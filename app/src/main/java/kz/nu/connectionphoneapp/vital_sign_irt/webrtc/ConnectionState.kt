package kz.nu.connectionphoneapp.vital_sign_irt.webrtc

import android.content.Context
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import org.webrtc.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit



// WebRTC Connection State
enum class ConnectionState {
    DISCONNECTED,
    CONNECTING,
    CONNECTED,
    FAILED
}

class WebRtcClient(private val context: Context, private val serverUrl: String) {
    private val TAG = "WebRtcClient"
    private val scope = CoroutineScope(Dispatchers.IO)
    
    // WebRTC
    private var peerConnectionFactory: PeerConnectionFactory? = null
    private var peerConnection: PeerConnection? = null
    private var eglBase: EglBase? = null
    private val _remoteVideoTrack = MutableStateFlow<VideoTrack?>(null)
    val remoteVideoTrack: StateFlow<VideoTrack?> = _remoteVideoTrack
    
    // Connection state
    private val _connectionState = MutableStateFlow(ConnectionState.DISCONNECTED)
    val connectionState: StateFlow<ConnectionState> = _connectionState

    // API
    private val api: WebRtcApi
    
    // Initialize WebRTC resources
    init {
        // Setup Retrofit
        val client = OkHttpClient.Builder()
            .connectTimeout(10000, TimeUnit.SECONDS)
            .readTimeout(30000, TimeUnit.SECONDS)
            .writeTimeout(30000, TimeUnit.SECONDS)
            .build()
            
        val retrofit = Retrofit.Builder()
            .baseUrl(serverUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            
        api = retrofit.create(WebRtcApi::class.java)
        
        eglBase = EglBase.create()
        initWebRtc()
    }
    
    private fun initWebRtc() {
        // Initialize PeerConnectionFactory
        val options = PeerConnectionFactory.InitializationOptions.builder(context)
            .setEnableInternalTracer(true)
            .createInitializationOptions()
        PeerConnectionFactory.initialize(options)
        
        val encoderFactory = DefaultVideoEncoderFactory(
            eglBase?.eglBaseContext,
            true,
            true
        )
        val decoderFactory = DefaultVideoDecoderFactory(eglBase?.eglBaseContext)
        
        val peerConnectionOptions = PeerConnectionFactory.Options()
        
        peerConnectionFactory = PeerConnectionFactory.builder()
            .setOptions(peerConnectionOptions)
            .setVideoEncoderFactory(encoderFactory)
            .setVideoDecoderFactory(decoderFactory)
            .createPeerConnectionFactory()
    }
    
    // Configure and create the peer connection
    private fun createPeerConnection(): PeerConnection? {
        val iceServers = listOf(
            PeerConnection.IceServer.builder("stun:stun.l.google.com:19302").createIceServer()
        )
        
        val rtcConfig = PeerConnection.RTCConfiguration(iceServers)
        rtcConfig.tcpCandidatePolicy = PeerConnection.TcpCandidatePolicy.DISABLED
        rtcConfig.bundlePolicy = PeerConnection.BundlePolicy.MAXBUNDLE
        rtcConfig.rtcpMuxPolicy = PeerConnection.RtcpMuxPolicy.REQUIRE
        rtcConfig.continualGatheringPolicy = PeerConnection.ContinualGatheringPolicy.GATHER_CONTINUALLY
        rtcConfig.keyType = PeerConnection.KeyType.ECDSA
        rtcConfig.sdpSemantics = PeerConnection.SdpSemantics.UNIFIED_PLAN
        
        val peerConnectionObserver = object : PeerConnection.Observer {
            override fun onSignalingChange(signalingState: PeerConnection.SignalingState?) {
                Log.d(TAG, "onSignalingChange: $signalingState")
            }
            
            override fun onIceConnectionChange(iceConnectionState: PeerConnection.IceConnectionState?) {
                Log.d(TAG, "onIceConnectionChange: $iceConnectionState")
                when (iceConnectionState) {
                    PeerConnection.IceConnectionState.CONNECTED -> {
                        _connectionState.value = ConnectionState.CONNECTED
                    }
                    PeerConnection.IceConnectionState.FAILED -> {
                        _connectionState.value = ConnectionState.FAILED
                    }
                    PeerConnection.IceConnectionState.DISCONNECTED -> {
                        _connectionState.value = ConnectionState.DISCONNECTED
                    }
                    else -> {}
                }
            }
            
            override fun onIceConnectionReceivingChange(receiving: Boolean) {
                Log.d(TAG, "onIceConnectionReceivingChange: $receiving")
            }
            
            override fun onIceGatheringChange(iceGatheringState: PeerConnection.IceGatheringState?) {
                Log.d(TAG, "onIceGatheringChange: $iceGatheringState")
            }
            
            override fun onIceCandidate(iceCandidate: IceCandidate?) {
                Log.d(TAG, "onIceCandidate: $iceCandidate")
                // For this implementation, we don't need to handle ICE candidates separately
                // as they're included in the SDP
            }
            
            override fun onIceCandidatesRemoved(iceCandidates: Array<out IceCandidate>?) {
                Log.d(TAG, "onIceCandidatesRemoved: ${iceCandidates?.size}")
            }
            
            override fun onAddStream(mediaStream: MediaStream?) {
                Log.d(TAG, "onAddStream: ${mediaStream?.videoTracks?.size}")
                // This is deprecated but might still be called in some implementations
            }
            
            override fun onRemoveStream(mediaStream: MediaStream?) {
                Log.d(TAG, "onRemoveStream")
            }
            
            override fun onDataChannel(dataChannel: DataChannel?) {
                Log.d(TAG, "onDataChannel: ${dataChannel?.label()}")
            }
            
            override fun onRenegotiationNeeded() {
                Log.d(TAG, "onRenegotiationNeeded")
            }

            override fun onAddTrack(rtpReceiver: RtpReceiver?, mediaStreams: Array<out MediaStream>?) {
                Log.e(TAG, "onAddTrack CALLED! Receiver: $rtpReceiver, Streams: ${mediaStreams?.size}") // Use Log.e for high visibility
                rtpReceiver?.track()?.let { track ->
                    Log.d(TAG, "Track received: kind=${track.kind()}, id=${track.id()}")
                    if (track.kind() == MediaStreamTrack.VIDEO_TRACK_KIND) { // Use constant
                        Log.i(TAG, "Video track found! Updating StateFlow.")
                        _remoteVideoTrack.value = track as VideoTrack
                    } else {
                        Log.w(TAG, "Received non-video track: ${track.kind()}")
                    }
                } ?: run {
                    Log.w(TAG, "onAddTrack called but track was null.")
                }
            }
        }
        
        return peerConnectionFactory?.createPeerConnection(rtcConfig, peerConnectionObserver)
    }
    
    // Start the WebRTC connection
    fun start() {
        scope.launch {
            try {
                _connectionState.value = ConnectionState.CONNECTING

                // Create peer connection if needed
                if (peerConnection == null) {
                    peerConnection = createPeerConnection()
                }

                // Add transceivers for receiving video
                val transceiver = peerConnection?.addTransceiver(
                    MediaStreamTrack.MediaType.MEDIA_TYPE_VIDEO,
                    RtpTransceiver.RtpTransceiverInit(RtpTransceiver.RtpTransceiverDirection.RECV_ONLY)
                )
                Log.d(TAG, "Added transceiver: $transceiver")

                // Create offer
                val constraints = MediaConstraints().apply {
                    mandatory.add(MediaConstraints.KeyValuePair("OfferToReceiveVideo", "true"))
                    mandatory.add(MediaConstraints.KeyValuePair("OfferToReceiveAudio", "false"))
                }

                peerConnection?.createOffer(object : SdpObserver {
                    override fun onCreateSuccess(p0: org.webrtc.SessionDescription?) {
                        Log.d(TAG, "Offer created successfully")
                        if (p0 != null) {
                            peerConnection?.setLocalDescription(object : SdpObserver {
                                override fun onCreateSuccess(p0: org.webrtc.SessionDescription?) { }

                                override fun onSetSuccess() {
                                    Log.d(TAG, "Local description set successfully")
                                    sendOffer(p0)
                                }
                                override fun onCreateFailure(p0: String?) {
                                    Log.e(TAG, "Local description create failed: $p0")
                                    _connectionState.value = ConnectionState.FAILED
                                }
                                override fun onSetFailure(p0: String?) {
                                    Log.e(TAG, "Local description set failed: $p0")
                                    _connectionState.value = ConnectionState.FAILED
                                }
                            }, SessionDescription(
                                org.webrtc.SessionDescription.Type.OFFER,
                                p0.description
                            ))
                        }
                    }


                    override fun onSetSuccess() {}
                    override fun onCreateFailure(error: String?) {
                        Log.e(TAG, "Offer creation failed: $error")
                        _connectionState.value = ConnectionState.FAILED
                    }
                    override fun onSetFailure(error: String?) {}
                }, constraints)
            } catch (e: Exception) {
                Log.e(TAG, "Error starting WebRTC: ${e.message}", e)
                _connectionState.value = ConnectionState.FAILED
            }
        }
    }
    
    // Send the offer to the server and process the answer
    private fun sendOffer(sdp: org.webrtc.SessionDescription?) {
        scope.launch {
            try {
                val apiOffer = SessionDescription(
                    sdp = sdp?.description.toString(),
                    type = sdp?.type.toString()
                )
                
                val response = api.sendOffer(apiOffer)
                
                if (response.isSuccessful && response.body() != null) {
                    val answer = response.body()!!
                    peerConnection?.setRemoteDescription(object : SdpObserver {
                       // override fun onCreateSuccess(p0: SessionDescription?) {}
                        override fun onCreateSuccess(p0: org.webrtc.SessionDescription?) {
                            TODO("Not yet implemented")
                        }

                        override fun onSetSuccess() {
                            Log.d(TAG, "Remote description set successfully")
                        }
                        override fun onCreateFailure(p0: String?) {}
                        override fun onSetFailure(error: String?) {
                            Log.e(TAG, "Remote description set failed: $error")
                            _connectionState.value = ConnectionState.FAILED
                        }
                    }, org.webrtc.SessionDescription(
                        org.webrtc.SessionDescription.Type.fromCanonicalForm(answer.type),
                        answer.sdp
                    ))
                } else {
                    Log.e(TAG, "API call failed: ${response.code()} - ${response.message()}")
                    _connectionState.value = ConnectionState.FAILED
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error in API call: ${e.message}", e)
                _connectionState.value = ConnectionState.FAILED
            }
        }
    }
    
    // Stop and clean up WebRTC resources
    fun stop() {
        scope.launch {
            try {
                peerConnection?.dispose()
                peerConnection = null
                _remoteVideoTrack.value = null
                _connectionState.value = ConnectionState.DISCONNECTED
            } catch (e: Exception) {
                Log.e(TAG, "Error stopping WebRTC: ${e.message}", e)
            }
        }
    }
    
    // Release resources when no longer needed
    fun release() {
        stop()
        peerConnectionFactory?.dispose()
        peerConnectionFactory = null
        eglBase?.release()
        eglBase = null
    }
    
    // Get EGL context for rendering
    fun getEglContext(): EglBase.Context? = eglBase?.eglBaseContext
}
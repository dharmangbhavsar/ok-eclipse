1) SpeechToText will take a microphone from the list and start taking input from that microphone. For that we need the exact name or ID of that microphone.
2) To get the exact name or ID of that microphone, I have made a DisplayingMicrophoneNames.py. This program displays names of available input and output devices and we will have to set the name of one of the microphones as mic_name in SpeechToText.py for it to work.
3) SpeechToText uses Google Speech API for speech reconition.
4) SpeechRecognition and pyaudio are required as dependencies for this to run. The programs are in Python 3.
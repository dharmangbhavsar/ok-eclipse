
import pyaudio 
import speech_recognition as sr
 
#ENTER THE NAME OF THE MIC THAT WE ARE GOING TO TEST ON
#I WILL BE MAKING ANOTHER PROGRAM TO DISPLAY NAMES OF MIC ON A SYSTEM
#FROM THAT LIST WE WILL BE SELECTING WHICH ONE WE WANT TO USE
#mic_name is the mic that we will be using,
mic_name = "Microphone (Realtek High Defini"
#Sample rate is how often values are recorded
sample_rate = 48000

#selecting buffer size. 
#Selecting 2048 could be increased.
chunk_size = 2048
#Initialize the recognizer
r = sr.Recognizer()
 
#generate a list of all audio cards/microphones
mic_list = sr.Microphone.list_microphone_names()
 
#the following loop aims to set the device ID of the mic that
#we specifically want to use to avoid ambiguity.
for i, microphone_name in enumerate(mic_list):
    #print(i, " ", microphone_name)
    if microphone_name == mic_name:
        device_id = i
 
#use the microphone as source for input. Here, we also specify 
#which device ID to specifically look for incase the microphone 
#is not working, an error will pop up saying "device_id undefined"
with sr.Microphone(device_index = device_id, sample_rate = sample_rate, chunk_size = chunk_size) as source:
    #It will be somewhat slow but it will work. 
    #not taking into consideration surrounding noise level
    r.adjust_for_ambient_noise(source)
    #listening
    audio = r.listen(source)
    # file = open('./command.cd', 'w') 
    try:
        text = r.recognize_google(audio)
        print(text)
     
   	#error when not recognised
     
    except sr.UnknownValueError:
        print("failed")
     
    except sr.RequestError as e:
        print("Could not request results from Google Speech Recognition service; {0}".format(e))

print("TUshita is not crazy?")
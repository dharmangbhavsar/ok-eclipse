import subprocess
import tempfile
#reading the input that you give.
with open('CodeConversion.java', 'r') as myfile:
    data=myfile.readlines()
#storing that input in java file
with open('source.java', 'w+') as myfile:
    #myfile.writelines(data)
    for a in data:
        myfile.writelines(a)
        myfile.writelines('\n')
        print(a)

#cmd = [ 'j2py', 'source.java', 'source1.py' ]
#p = subprocess.Popen( cmd, stdout=subprocess.PIPE )
#out = p.communicate()
#print('Here is the output')
#print(out)

#running the process in terminal to convert code.
result = subprocess.run(['j2py', 'source.java', 'code.py'], stdout=subprocess.PIPE)
print("Dharmang")
print(result)

#reading the converted python file
with open('code.py', 'r') as myfile:
    str = myfile.readlines()
#just printing the file to check.
for a in str:
    print(a)
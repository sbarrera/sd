
#Directories definitions
## EDIT YOUR SERVER AND CLIENT directory 

import copy
import os
import subprocess
import time

base_dir = os.getcwd()
os.chdir(base_dir)

##Replace with your Server and Client Directory 
listdirsServerStudents = ["out/artifacts/sevenhalf_bank_source", "out/artifacts/sevenhalf_bank_selector_source"]
listdirsClientStudents = ["out/artifacts/sevenhalf_player_source"]

##  Methods for compiling java packages.
## Recursively search for .java files and append packages names to class names.
## Do not touch

def crawler(package,base_dir):
    print base_dir+package
    listfiles = os.listdir(base_dir+package)
    listjava = []
    for ff in listfiles:
        if (os.path.isdir(base_dir+package+'/'+ff)) and  (not ff.startswith('.')) :
            l = [ ff+'/'+x for x in crawler(package +'/'+ ff,base_dir)]
            listjava.extend(l)
        else:
            if (ff.endswith(".java")):
                listjava.append(ff)
    return listjava           


def compile_java(directory,base_dir):
    os.chdir(base_dir+'/'+directory)
    listjava=crawler('',base_dir+'/'+directory)    
    cmd = 'javac '+ ' '.join(listjava)
    print cmd
    proc = subprocess.call(cmd, shell=True)
    os.chdir(base_dir)
    if proc != 0 :
        print "Error Compiling"
    return proc

## Function for testing Servers and Clients.
## Do not touch

def test(client_dir,server_dir,res_out,tests):
    results={}  
    for t in tests:
        log_server_dir = t['logpath'] 
        results[t['name']]=[]
        filedeck = t['deck']
        clientparam = t['client']
        server = subprocess.Popen(["java Server -p 1212 -b 100 -f " + filedeck], cwd=server_dir, shell=True)
        #wait certain time for server waking up.
        time.sleep(1)
        server.poll()
        if server.returncode != None:
            print "Error executing Server"
        else:
            clients=[]
            for c in clientparam:
                proc = subprocess.Popen(["java Client -s localhost -p 1212 " + c],cwd=client_dir, shell=True)
                proc.wait()   
                if proc.returncode==0:
                    clients.append(proc)
                else:
                    print "Error executing Client"
            i=0
            for c in clients:
                cmd = "diff "+ server_dir + "/ServerThread-" +str(i)+".log "+ log_server_dir+"ServerThread-" +str(i)+".log" + " > "+res_out+"ServerThread-" +str(i)+'.out'
                subprocess.call(cmd, shell=True) 
                b = os.path.getsize(res_out+"ServerThread-" +str(i)+'.out')
                if b==0:
                    print (t['name']+"-"+str(i)+") TEST PASSED")
                else:
                    print (t['name']+"-"+str(i)+") TEST FAILED")
                i=i+1
                results[t['name']].append(b);
            server.kill()
    return results

def compile():
    ##COMPILE ALL FILES
    ## Do not touch
    totaldirs = copy.deepcopy(listdirsClientStudents)
    totaldirs.extend(listdirsServerStudents)
    res = [compile_java(x,base_dir) for x in totaldirs]
    print "Compiling Done: " + str(res)

def test():
    #Definition of test
    #name:
    #deck: deck used by server in this test
    #client: list of different arguments, each argument is a differnt client execution.
    #logpath: Path where right execution logs are, one log for each client execution.
    ## EDIT deck.txt location and path with correct log for each test


    test0={"name": "basic0",
           "deck": "/Users/eloi/Dropbox/SD2015/Practica1/deck0.txt",
           "client":["-a 6","-a 5","-a 7.5"],
           "logpath":"/Users/eloi/Dropbox/SD2015/Practica1/deck0/" }

    test1={"name": "basic1",
           "deck": "/Users/eloi/Dropbox/SD2015/Practica1/deck.txt",
           "client":["-a 6","-a 5","-a 7.5"],
           "logpath":"/Users/eloi/Dropbox/SD2015/Practica1/deck/" }

    tests=[test0,test1]

    #First TESTING Servers

    ## EDIT your server location. 

    print "Testing all student Servers"
    client_dir_teacher= '/Users/eloi/Dropbox/SD2015/Practica1/Exemple/src/'
    s={x:test(client_dir_teacher,x,x,tests) for x in listdirsServerStudents}
    print s

    ## EDIT your client location.

    #Second Testing Clients.
    print "Testing all student Clients"
    server_dir_teacher= '/Users/eloi/Dropbox/SD2015/Practica1/Exemple/src/'
    c={x:test(x,server_dir_teacher,x,tests) for x in listdirsClientStudents}
    print c

if __name__ == "__main__":
    compile()

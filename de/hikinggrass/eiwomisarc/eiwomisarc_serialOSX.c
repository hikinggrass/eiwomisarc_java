#include "eiwomisarc_serialOSX.h"
#include <stdio.h>
/* RS-232 */
#include <fcntl.h>   /* file control definitions */
#include <errno.h>   /* error number definitions */
#include <termios.h> /* POSIX terminal control definitions */

#define BUFFSIZE 6
int globalSerialPort = -1;
unsigned char globalBuffer[BUFFSIZE];

jint Java_de_hikinggrass_eiwomisarc_Serial_externFuntion(JNIEnv *env, jobject iObj, jstring jMsg) {
	return 42;
}


void Java_de_hikinggrass_eiwomisarc_Serial_externTest (JNIEnv *env, jobject iObj, jint jMsg) {
	printf("int: %i", (int)jMsg);
}

jint Java_de_hikinggrass_eiwomisarc_Serial_externOpenPort(JNIEnv *env, jobject iObj, jstring serialPort, jint baudRate) {
	
	const char* pPort = (*env)->GetStringUTFChars(env, serialPort, JNI_FALSE);
	int pBaud = (int)baudRate;
	
	globalSerialPort = open(pPort, O_RDWR | O_NOCTTY | O_NDELAY);
	
	if (globalSerialPort == -1) {
		return -1;
	} else {
		fcntl(globalSerialPort, F_SETFL, 0);
		
		struct termios options;
		
		/* get the current options for the port */
		tcgetattr(globalSerialPort, &options);
				
		/* set baudrate */
		cfsetispeed(&options, pBaud);
		cfsetospeed(&options, pBaud);
		
		/* enable the receiver and set local mode */
		options.c_cflag |= (CLOCAL | CREAD);
		
		/* set the new options for the port */
		tcsetattr(globalSerialPort, TCSANOW, &options);
		
		return 0;
	}
}

void Java_de_hikinggrass_eiwomisarc_Serial_externClosePort(JNIEnv *env, jobject iObj) {
	if(globalSerialPort != -1) {
		close(globalSerialPort);
	}
}

jint Java_de_hikinggrass_eiwomisarc_Serial_externWrite(JNIEnv *env, jobject iObj, jbyteArray buffer) {
	
    jsize len = (*env)->GetArrayLength(env, buffer);
    int i = 0;
    jbyte *body = (*env)->GetByteArrayElements(env, buffer, 0);
    for(i=0;i<len;i++) {
    	globalBuffer[i] = (unsigned char)(body[i]&0x000000ff);
    }
    int n = write(globalSerialPort, globalBuffer, len);
	
    (*env)->ReleaseByteArrayElements(env, buffer, body, 0);
    if (n < 0) {
    	//write failed
    	return -1;
    } else {
    	//success
    	return 0;
    }
}
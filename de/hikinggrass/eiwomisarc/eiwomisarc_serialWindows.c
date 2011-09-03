#include "eiwomisarc_serialWindows.h"
#include <stdio.h>
/* RS-232 */
#include <fcntl.h>   /* file control definitions */
#include <errno.h>   /* error number definitions */
#include <windows.h>


#define BUFFSIZE 6
HANDLE globalSerialPort;
unsigned char globalBuffer[BUFFSIZE];
/*
 * Class:     de_hikinggrass_eiwomisarc_Serial
 * Method:    externFuntion
 * Signature: (Ljava/lang/String;)I
 */
jint JNICALL Java_de_hikinggrass_eiwomisarc_Serial_externFuntion(JNIEnv *env, jobject obj, jstring str) {
	return 42;
}

/*
 * Class:     de_hikinggrass_eiwomisarc_Serial
 * Method:    externTest
 * Signature: (I)V
 */
void JNICALL Java_de_hikinggrass_eiwomisarc_Serial_externTest(JNIEnv *env, jobject obj, jint number) {

}

/*
 * Class:     de_hikinggrass_eiwomisarc_Serial
 * Method:    externOpenPort
 * Signature: (Ljava/lang/String;I)I
 */
jint JNICALL Java_de_hikinggrass_eiwomisarc_Serial_externOpenPort(JNIEnv *env, jobject obj, jstring serialPort, jint baudRate){
	const char* pPort = (*env)->GetStringUTFChars(env, serialPort, JNI_FALSE);
	int pBaud = (int)baudRate;
	
	globalSerialPort = CreateFile(pPort,
								GENERIC_READ | GENERIC_WRITE,
								0,
								0,
								OPEN_EXISTING,
								FILE_ATTRIBUTE_NORMAL,
								0);
	if(globalSerialPort==INVALID_HANDLE_VALUE){
		if(GetLastError()==ERROR_FILE_NOT_FOUND){
			//serial port does not exist. Inform user.
			return -1;
		}
		return -1;
	}

	DCB dcbSerialParams = {0};
	dcbSerialParams.DCBlength=sizeof(dcbSerialParams);
	if (!GetCommState(globalSerialPort, &dcbSerialParams)) {
		//error getting state
		return -1;
	}
	dcbSerialParams.BaudRate=CBR_9600; //FIXME!!!!
	dcbSerialParams.ByteSize=8;
	dcbSerialParams.StopBits=ONESTOPBIT;
	dcbSerialParams.Parity=NOPARITY;
	if(!SetCommState(globalSerialPort, &dcbSerialParams)){
		//error setting serial port state
		return -1;
	}
	
	return 0;
}

/*
 * Class:     de_hikinggrass_eiwomisarc_Serial
 * Method:    externClosePort
 * Signature: ()V
 */
void JNICALL Java_de_hikinggrass_eiwomisarc_Serial_externClosePort(JNIEnv *env, jobject obj){
	CloseHandle(globalSerialPort);
}

/*
 * Class:     de_hikinggrass_eiwomisarc_Serial
 * Method:    externWrite
 * Signature: ([B)I
 */
jint JNICALL Java_de_hikinggrass_eiwomisarc_Serial_externWrite(JNIEnv *env, jobject obj, jbyteArray buffer){
	jsize len = (*env)->GetArrayLength(env, buffer);
    int i = 0;
    jbyte *body = (*env)->GetByteArrayElements(env, buffer, 0);
    for(i=0;i<len;i++) {
    	globalBuffer[i] = (unsigned char)(body[i]&0x000000ff);
    }
	
	int n;
	DWORD dwBytesRead = 0;
	if(!WriteFile(globalSerialPort, globalBuffer, len, &dwBytesRead, NULL)){
		n = -1;
	} else {
		n = 0;
	}

    (*env)->ReleaseByteArrayElements(env, buffer, body, 0);
    if (n < 0) {
    	//write failed
    	return -1;
    } else {
    	//success
    	return 0;
    }
}


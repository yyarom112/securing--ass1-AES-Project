package com.company;

public class Hacker {

    private String input;
    private String output;
    private String chipher;
    private byte[][][] key_arr;
    AES aes;


    public Hacker(String input,String chipher, String output) {
        this.chipher = chipher;
        this.input = input;
        this.output = output;
        aes=new AES("-e",input,null,null);
        generateKeys();
        aes.setKey_arr_feild(this.key_arr);
    }

    private void generateKeys() {
        key_arr=new byte[3][4][4];
        byte[][][] input_arr = aes.swapColumAndRows(aes.spliteToState(aes.file_to_bytes(chipher)));
        key_arr[2]=input_arr[0];

    }

    public void exec(){
        byte[] key3=aes.exec();
        byte[] key=new byte[48];
        for(int i=0;i<32;i++){
            key[i]=0;
        }
        for(int i=32 , j=0;i<48;i++, j++){
            key[i]=key3[j];
        }
        aes.bytes_to_file(output,key);
    }
}

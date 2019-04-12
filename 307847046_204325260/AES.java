package com.company;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.lang.Object;

public class AES {
    private String command;
    private String input;
    private String output;
    private String keys;
    private byte[][][] key_arr_feild;

    public AES(String command, String input, String output, String keys) {
        this.command = command;
        this.input = input;
        this.output = output;
        this.keys = keys;
    }


    public byte[] exec() {
        byte[][][] output_arr;
        byte[][][] key_arr;
        if(keys!=null)
          key_arr = swapColumAndRows(splitToKeys(file_to_bytes(keys)));
        else
            key_arr=this.key_arr_feild;

        byte[][][] input_arr = swapColumAndRows(spliteToState(file_to_bytes(input)));
        if (key_arr == null || input_arr == null)
            System.err.println("The key or input file is not good\n");

        if (command.contains("d"))
            output_arr = decrypt(key_arr, input_arr);
        else
            output_arr = encrypt(key_arr, input_arr);
        if(keys!=null){
            bytes_to_file(this.output,arrayingOfArrays(swapColumAndRows(output_arr)));
            return null;
        }
        else{
            return arrayingOfArrays(swapColumAndRows(output_arr));
        }

    }

    private byte[][][] encrypt(byte[][][] key_arr, byte[][][] input_arr) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < input_arr.length; j++) {
                int h=0;
                for(int k=0;k<input_arr[j].length;k++){
                        input_arr[j][k]=leftrotate(input_arr[j][k],h);
                        h++;
                }
                addRoundKey(input_arr[j],key_arr[i]);
            }
        }
        return input_arr;
    }

    private byte[][][] decrypt(byte[][][] key_arr, byte[][][] input_arr) {
        for(int i=2;i>=0;i--){
            for(int j=0;j<input_arr.length;j++){
                addRoundKey(input_arr[j],key_arr[i]);
                int h=0;
                for(int k=0;k<input_arr[j].length;k++){
                        input_arr[j][k]=rightrotate(input_arr[j][k],h);
                        h++;
                }
            }
        }
        return input_arr;
    }

    public byte[][][] swapColumAndRows(byte[][][] in) {
        byte[][][] output=new byte[in.length][4][4];
        for (int i = 0; i < in.length; i++) {
            byte[][] tmp = new byte[4][4];
            for (int j = 0; j < in[i].length; j++) {
                for (int k = 0; k < in[i][j].length; k++) {
                    tmp[k][j]=in[i][j][k];
                }
            }
            output[i]=tmp;
        }
        return output;
    }





    protected byte[] file_to_bytes(String path) {
        Path fileLocation = Paths.get(path);
        try {
            return Files.readAllBytes(fileLocation);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    protected boolean bytes_to_file(String path, byte[] output) {
        FileOutputStream outFile = null;
        try {
            outFile = new FileOutputStream(path);
            outFile.write(output);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (outFile != null) {
                try {
                    outFile.close();
                    return true;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    public byte[][][] spliteToState(byte[] in) {
        if (in == null)
            return null;
        if (in.length % 16 != 0) {
            System.err.println("spliteToState: The input file is not good\n");
            return null;
        }
        byte[][][] output = new byte[in.length / 16][4][4];
        for(int j=0;j<in.length;j++){
            output[j/16][(j%16)/4][(j%16)%4]=in[j];
        }
        return output;
    }

    public byte[][][] splitToKeys(byte[] in){
        if (in.length != 48){
            System.err.println("splitToKeys-The key or input file is not good\n");
            return null;
        }
        byte[][][] output=new byte[3][4][4];
        for(int i=0;i<in.length;i++){
            output[i/16][(i%16)/4][(i%16)%4]=in[i];
        }
        return output;
    }


    private byte[] leftrotate(byte[] arr, int times) {
        assert (arr.length == 4);
        if (times % 4 == 0) {
            return arr;
        }
        while (times > 0) {
            byte temp = arr[0];
            for (int i = 0; i < arr.length - 1; i++) {
                arr[i] = arr[i + 1];
            }
            arr[arr.length - 1] = temp;
            --times;
        }
        return arr;
    }

    private byte[] rightrotate(byte[] arr, int times) {
        if (arr.length == 0 || arr.length == 1 || times % 4 == 0) {
            return arr;
        }
        while (times > 0) {
            byte temp = arr[arr.length - 1];
            for (int i = arr.length - 1; i > 0; i--) {
                arr[i] = arr[i - 1];
            }
            arr[0] = temp;
            --times;
        }
        return arr;
    }

    private void addRoundKey(byte[][] msg, byte[][] key)
    {
        for(int i=0;i<key.length;i++){
            for(int j=0;j<key[i].length;j++){
                msg[i][j]^=key[i][j];
            }
        }
    }

    private byte[] arrayingOfArrays(byte[][][] in){
        byte[] output=new byte[in.length*16];
        for(int j=0;j<output.length;j++){
            output[j]=in[j/16][(j%16)/4][(j%16)%4];
        }
            return output;
    }

    public void setKey_arr_feild(byte[][][] key_arr_feild) {
        this.key_arr_feild = key_arr_feild;
    }
}

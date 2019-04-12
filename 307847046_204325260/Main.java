package com.company;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {

    public static void main(String[] args) {
        String command="",keys="",input="",output="",cipher="",toCompare="";
        String err="\nErrors:\n";
        for(int i=1;i<args.length;i++){
            switch (args[i]){
                case "-k":
                case "k":
                    i++;
                    keys=args[i];
                    break;
                case "-i":
                case "i":
                    i++;
                    input=args[i];
                    break;
                case "-o":
                case "o":
                    i++;
                    output=args[i];
                    break;
                case "-c":
                case "c":
                    i++;
                    cipher=args[i];
                    break;
                case "-t":
                case "t":
                    i++;
                    toCompare=args[i];
                    break;
                default:
                    System.err.println("The argument "+args[i]+" is not recognized\n");
                    return;

            }
        }
        if(keys.equals("") && cipher.equals(""))
            err+="The keys path or ciphers path is missing\n";
        if(input.equals(""))
            err+="The input path is missing\n";
        if(output.equals(""))
            err+="The input path is missing\n";
        if(!err.equals("\nErrors:\n"))
            System.err.println(err);

        if(args[0].lastIndexOf("d")!=-1 || args[0].lastIndexOf("e")!=-1){
            AES aes=new AES(args[0],input,output,keys);
            aes.exec();
        }
        else if(args[0].lastIndexOf("b")!=-1){
            Hacker hacker=new Hacker(input,cipher,output);
            hacker.exec();
        }
        else
            System.err.println("The command"+args[0]+"is unknown\n");
        if(!toCompare.equals("")){
            test(output,toCompare);
        }

    }


    public static void test(String path1, String path2){
        boolean test=true;
        byte[] first=null,second=null;
        Path fileLocation1 = Paths.get(path1);
        Path fileLocation2 = Paths.get(path2);

        try {
            first= Files.readAllBytes(fileLocation1);
            second= Files.readAllBytes(fileLocation2);

        } catch (IOException e) {
            e.printStackTrace();
        }
        if(first!=null && second!= null && first.length!=second.length)
            test=false;
        for(int i=0;test && i<first.length ; i++){
            if(first[i]!=second[i])
                test=false;
        }
        if(test)
            System.out.println("The files are the same");
        else
            System.err.println("The files are different");


    }
}

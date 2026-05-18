package com.manual.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

import com.manual.model.Configurations;
import com.manual.model.Scene;

public class ManualFunctions 
{
	public static String Preview(String selected_sports,String scene, PrintWriter print_writer, boolean isGraphicOnScreen,Configurations session_Configurations) throws InterruptedException
    {
		TimeUnit.MILLISECONDS.sleep(500);
		String path = "C:\\Temp\\Preview.png";
		//scene = scene.replace(".sum", "");
		switch(selected_sports.toUpperCase()) {
			case "BADMINTON":
				if(scene.equalsIgnoreCase("Bug_OneLine")||
						scene.equalsIgnoreCase("Bug_TwoLine")) {
					print_writer.println("LAYER1*EVEREST*STAGE*DIRECTOR*TrumpLoop START;");
				} 
				break;
		}
		
		if(session_Configurations.getIpAddressEverest().equalsIgnoreCase("LOCALHOST")) {
			path = "C:\\Temp\\Preview.png";
		}else {
			path = "\\\\" + session_Configurations.getIpAddressEverest() + "\\c\\Temp\\Preview.png";
		}
		
//		if(session_Configurations.getIpAddressEverest().equalsIgnoreCase("localhost") || session_Configurations.getIpAddressScenes().equalsIgnoreCase("")) {
//			print_writer.println("LAYER6*EVEREST*SINGLE_SCENE LOAD " + ManualUtil.SCENE_DIRECTORY + ManualUtil.SCENES_DIRECTORY + scene + ".sum;");
//		}else {
//			print_writer.println("LAYER6*EVEREST*SINGLE_SCENE LOAD " + "//" + session_Configurations.getIpAddressScenes() +
//					"//" + ManualUtil.SCENE_DIRECTORY.replace("C:", "c") + ManualUtil.SCENES_DIRECTORY + scene + ".sum;");
//		}
//		
//		print_writer.println("LAYER6*EVEREST*STAGE*DIRECTOR*In SHOW 190;");
//		print_writer.println("LAYER6*EVEREST*GLOBAL OFFSCREEN_SNAPSHOT ON;");
		
		print_writer.println("LAYER1*EVEREST*GLOBAL PREVIEW ON;");
		print_writer.println("LAYER1*EVEREST*STAGE*DIRECTOR*In STOP;");
		print_writer.println("LAYER1*EVEREST*STAGE*DIRECTOR*Out STOP;");
		
		if (scene.contains("Bug")||scene.contains("LT")) {
			print_writer.println("LAYER1*EVEREST*STAGE*DIRECTOR*In SHOW 39.0;");
		}else {
			print_writer.println("LAYER1*EVEREST*STAGE*DIRECTOR*In SHOW 144.0;");
		}
		print_writer.println("LAYER1*EVEREST*STAGE*DIRECTOR*Out SHOW 0.0;");
		print_writer.println("LAYER1*EVEREST*GLOBAL SNAPSHOT_PATH " + path + ";");
		print_writer.println("LAYER1*EVEREST*GLOBAL SNAPSHOT 1920 1080;");
		print_writer.println("LAYER1*EVEREST*STAGE*DIRECTOR*Out SHOW 0.0;");
		if(!isGraphicOnScreen) {
			print_writer.println("LAYER1*EVEREST*STAGE*DIRECTOR*In SHOW 0.0;");	
		}
		print_writer.println("LAYER1*EVEREST*GLOBAL PREVIEW OFF;");	
		
        return "";
    }
	public static String checkConnection(String ip, int port, int timeout) {
        Socket socket = new Socket();
        try {
            socket.connect(new InetSocketAddress(ip, port), timeout);
            return "Connected";
        } catch (SocketTimeoutException e) {
            return "Disconnected";
        } catch (IOException e) {
            return "Disconnected";
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
	
}
package com.manual.controller;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.manual.model.Configurations;
import com.manual.model.Container;
import com.manual.model.ContainerData;
import com.manual.model.ImageData;
import com.manual.model.Scene;
import com.manual.service.ManualService;
import com.manual.util.ManualFunctions;
import com.manual.util.ManualUtil;

@Controller
public class IndexController 
{
	@Autowired
	ManualService manualService;
	public static Socket session_socket;
	public static Configurations session_Configurations;
	public static PrintWriter print_writer;
	public static ContainerData session_Data;
	public static String expiry_date = "2026-11-21";
	public static String current_date;
	String session_selected_sports,session_selected_PreviewIp;
	String Data;
	String Scene;
	String connection;
	public static boolean IsGraphicOnScreen = false;
	boolean is_previous_data = false;
	List<ImageData> imgdata = new ArrayList<ImageData>();
	public final ObjectMapper objectMapper = new ObjectMapper();
	
	@RequestMapping(value = "/contact", method = RequestMethod.GET)
	public String showContactPage() {
	    return "contact"; // Loads contact.jsp
	}
	
	@RequestMapping(value = {"/","/initialise"}, method={RequestMethod.GET,RequestMethod.POST}) 
	public String initialisePage(ModelMap model,
			@ModelAttribute("expiryDate") String expiryDate) throws JAXBException, MalformedURLException, IOException  
	{
		
		if(current_date == null || current_date.isEmpty()) {
			current_date = getOnlineCurrentDate();
		}
		
		if(new File(ManualUtil.CONFIGURATION_DIRECTORY + ManualUtil.OUTPUT_XML).exists()) {
			session_Configurations = (Configurations)JAXBContext.newInstance(Configurations.class).createUnmarshaller().unmarshal(
					new File(ManualUtil.CONFIGURATION_DIRECTORY  + ManualUtil.OUTPUT_XML));
		} else {
			session_Configurations = new Configurations();
			JAXBContext.newInstance(Configurations.class).createMarshaller().marshal(session_Configurations, 
					new File(ManualUtil.CONFIGURATION_DIRECTORY + ManualUtil.OUTPUT_XML));
		}
		model.addAttribute("session_Configurations",session_Configurations);
		
		return "initialise";
	}

	@RequestMapping(value = {"/manual"}, method={RequestMethod.GET,RequestMethod.POST}) 
	public String manualPage(ModelMap model, MultipartHttpServletRequest request,
			@ModelAttribute("expiryDate") String expiryDate,
			@RequestParam(value = "select_sports", required = false, defaultValue = "") String select_sports,
			@RequestParam(value = "vizIPAddressEverest", required = false, defaultValue = "") String vizIPAddressEverest,
			@RequestParam(value = "vizIPAddressScenes", required = false, defaultValue = "") String vizIPAddressScenes,
			@RequestParam(value = "vizPortNumber", required = false, defaultValue = "") int vizPortNumber)
			throws UnknownHostException,JAXBException, IOException,IllegalAccessException,InvocationTargetException, URISyntaxException, ParseException
	{
		
		if(current_date == null || current_date.isEmpty()) {
			
			model.addAttribute("error_message","You must be connected to the internet online");
			return "error";
		
		} else if(new SimpleDateFormat("yyyy-MM-dd").parse(expiry_date).before(new SimpleDateFormat("yyyy-MM-dd").parse(current_date))) {
			
			model.addAttribute("error_message","This software has expired");
			return "error";
			
		}else {
			
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			
			LocalDate date1 = LocalDate.parse(current_date, dtf);
			LocalDate date2 = LocalDate.parse(expiry_date, dtf);
			
			long daysBetween = ChronoUnit.DAYS.between(date1, date2);
			
			expiryDate = String.valueOf(daysBetween);
			
			session_selected_sports = select_sports;
			session_selected_PreviewIp = vizIPAddressEverest;
			
			if(!vizIPAddressEverest.trim().isEmpty() && vizPortNumber != 0) {
				session_socket = new Socket(vizIPAddressEverest, Integer.valueOf(vizPortNumber));
				print_writer = new PrintWriter(session_socket.getOutputStream(), true);
			}
			
			session_Configurations = new Configurations(vizIPAddressEverest,vizIPAddressScenes, vizPortNumber);
			
			JAXBContext.newInstance(Configurations.class).createMarshaller().marshal(session_Configurations, 
					new File(ManualUtil.CONFIGURATION_DIRECTORY + ManualUtil.OUTPUT_XML));
			
			
			if(session_Configurations.getIpAddressScenes().equalsIgnoreCase("localhost") || session_Configurations.getIpAddressScenes().equalsIgnoreCase("")) {
				model.addAttribute("session_viz_scenes", new File(ManualUtil.SCENE_DIRECTORY + ManualUtil.SCENES_DIRECTORY).listFiles(new FileFilter() {
					@Override
				    public boolean accept(File pathname) {
				        String name = pathname.getName().toLowerCase();
				        return name.endsWith(".sum") && pathname.isFile();
				    }
				}));
				
				model.addAttribute("scene_files", new File(ManualUtil.MANUAL_DIRECTORY + ManualUtil.DATA_DIRECTORY).listFiles(new FileFilter() {
					@Override
				    public boolean accept(File pathname) {
				        String name = pathname.getName().toLowerCase();
				        return name.endsWith(".xml") && pathname.isFile();
				    }
				}));
			}else {
				model.addAttribute("session_viz_scenes", new File("//" + session_Configurations.getIpAddressScenes() + "//" + ManualUtil.SCENE_DIRECTORY.replace("C:", "c") + ManualUtil.SCENES_DIRECTORY).listFiles(new FileFilter() {
					@Override
				    public boolean accept(File pathname) {
				        String name = pathname.getName().toLowerCase();
				        return name.endsWith(".sum") && pathname.isFile();
				    }
				}));
				
				model.addAttribute("scene_files", new File("//" + session_Configurations.getIpAddressScenes() + "//" + ManualUtil.MANUAL_DIRECTORY.replace("C:", "c") + ManualUtil.DATA_DIRECTORY).listFiles(new FileFilter() {
					@Override
				    public boolean accept(File pathname) {
				        String name = pathname.getName().toLowerCase();
				        return name.endsWith(".xml") && pathname.isFile();
				    }
				}));
			}
				
			model.addAttribute("session_selected_sports", session_selected_sports);
			model.addAttribute("session_Data", session_Data);
			return "manual";
		}
	}
	
	@RequestMapping(value = {"/back_to_manual"}, method={RequestMethod.GET,RequestMethod.POST}) 
	public String backToManualPage(ModelMap model,
			@ModelAttribute("expiryDate") String expiryDate) throws ParseException
	{
		if(current_date == null || current_date.isEmpty()) {
			
			model.addAttribute("error_message","You must be connected to the internet online");
			return "error";
		
		} else if(new SimpleDateFormat("yyyy-MM-dd").parse(expiry_date).before(new SimpleDateFormat("yyyy-MM-dd").parse(current_date))) {
			
			model.addAttribute("error_message","This software has expired");
			return "error";
			
		}else {
			
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			
			LocalDate date1 = LocalDate.parse(current_date, dtf);
			LocalDate date2 = LocalDate.parse(expiry_date, dtf);
			
			long daysBetween = ChronoUnit.DAYS.between(date1, date2);
			
			expiryDate = String.valueOf(daysBetween);
			
			if(session_Configurations.getIpAddressScenes().equalsIgnoreCase("localhost") || session_Configurations.getIpAddressScenes().equalsIgnoreCase("")) {
				model.addAttribute("session_viz_scenes", new File(ManualUtil.SCENE_DIRECTORY + ManualUtil.SCENES_DIRECTORY).listFiles(new FileFilter() {
					@Override
				    public boolean accept(File pathname) {
				        String name = pathname.getName().toLowerCase();
				        return name.endsWith(".sum") && pathname.isFile();
				    }
				}));
				
				model.addAttribute("scene_files", new File(ManualUtil.MANUAL_DIRECTORY + ManualUtil.DATA_DIRECTORY).listFiles(new FileFilter() {
					@Override
				    public boolean accept(File pathname) {
				        String name = pathname.getName().toLowerCase();
				        return name.endsWith(".xml") && pathname.isFile();
				    }
				}));
			}else {
				model.addAttribute("session_viz_scenes", new File("//" + session_Configurations.getIpAddressScenes() + "//" + ManualUtil.SCENE_DIRECTORY.replace("C:", "c") + ManualUtil.SCENES_DIRECTORY).listFiles(new FileFilter() {
					@Override
				    public boolean accept(File pathname) {
				        String name = pathname.getName().toLowerCase();
				        return name.endsWith(".sum") && pathname.isFile();
				    }
				}));
				
				model.addAttribute("scene_files", new File("//" + session_Configurations.getIpAddressScenes() + "//" + ManualUtil.MANUAL_DIRECTORY.replace("C:", "c") + ManualUtil.DATA_DIRECTORY).listFiles(new FileFilter() {
					@Override
				    public boolean accept(File pathname) {
				        String name = pathname.getName().toLowerCase();
				        return name.endsWith(".xml") && pathname.isFile();
				    }
				}));
			}
			
			model.addAttribute("session_selected_sports", session_selected_sports);
			model.addAttribute("session_Data", session_Data);
			return "manual";
		}
	}
	
	@RequestMapping(value = {"/save_data","/uploadFileToManual","/preview"}, method={RequestMethod.GET,RequestMethod.POST})
	public @ResponseBody String uploadFormDataToSessionObjects(MultipartHttpServletRequest request)
					throws IllegalAccessException, InvocationTargetException, IOException, JAXBException, NumberFormatException, InterruptedException
	{
		File file;
		MultipartFile mpf;
		String whichFile = "",file_name = "";
			if (request.getRequestURI().contains("save_data")||request.getRequestURI().contains("preview")) {
				List<Container> containers = new ArrayList<Container>();
				containers.clear();
				for (Entry<String, String[]> entry : request.getParameterMap().entrySet()) {
					if(entry.getKey().contains("previous_xml_data") || entry.getKey().contains("selectedScene") 
							|| entry.getKey().contains("manual_file_timestamp") 
							|| entry.getKey().contains("select_sport")) {
					}
					else if(entry.getKey().contains("scenePath")) {
						if(!entry.getKey().contains("0_scenePath")) {
							if(is_previous_data == true) {
								
								containers.add(new Container(Integer.valueOf(0), entry.getKey().replaceFirst("", "0_"), session_Data.getContainers().get(0).getContainer_value()));
							}else {
								containers.add(new Container(Integer.valueOf(0), entry.getKey().replaceFirst("", "0_"), entry.getValue()[0]));
							}
						}
					}
					else if(entry.getKey().contains("Logo")||entry.getKey().contains("Sponsor") || entry.getKey().contains("Image")) {
						if(imgdata.size() > 0) {
							for(int i=0;i<imgdata.size();i++) {
								if(imgdata.get(i).getImageId().equalsIgnoreCase(entry.getKey())) {
									containers.add(new Container(Integer.valueOf(entry.getKey().split("_")[0]), entry.getKey(), imgdata.get(i).getImagePath()));
									break;
								}
							}
							
							SetData(containers, is_previous_data, imgdata,entry.getKey(),entry.getValue()[0]);
							
							
						}else {
							if(is_previous_data == true) {
								for(int i = 0;i< session_Data.getContainers().size();i++) {
									if(session_Data.getContainers().get(i).getContainer_key().equalsIgnoreCase(entry.getKey())) {
										containers.add(new Container(Integer.valueOf(entry.getKey().split("_")[0]), entry.getKey(), session_Data.getContainers().get(i).getContainer_value()));
										//break;
									}
								}
							}else {
								containers.add(new Container(Integer.valueOf(entry.getKey().split("_")[0]), entry.getKey(), entry.getValue()[0]));
							}
						}
					}
					else if(entry.getKey().contains("file_name")) {
						file_name = entry.getValue()[0];
					}
					else {
						containers.add(new Container(Integer.valueOf(entry.getKey().split("_")[0]), entry.getKey(), entry.getValue()[0]));
					}		
				}
				Collections.sort(containers);
				if(request.getRequestURI().contains("save_data")) {
					
					String basePath = session_Configurations.getIpAddressScenes().equalsIgnoreCase("localhost") || 
	                  session_Configurations.getIpAddressScenes().equalsIgnoreCase("") 
	                  ? ManualUtil.MANUAL_DIRECTORY + ManualUtil.DATA_DIRECTORY 
	                  : "//" + session_Configurations.getIpAddressScenes() + "//" + 
	                    ManualUtil.MANUAL_DIRECTORY.replace("C:", "c") + ManualUtil.DATA_DIRECTORY;

					File Xmlfile = new File(basePath + file_name + ManualUtil.XML);
					File parentDir = Xmlfile.getParentFile();
					if (!parentDir.exists()) parentDir.mkdirs();
			
					JAXBContext.newInstance(ContainerData.class).createMarshaller().marshal(new ContainerData(containers), Xmlfile);	
					
				}else if (request.getRequestURI().contains("preview")) {
					for(int i = 1; i < containers.size() ; i++) {
						if(!session_Configurations.getIpAddressEverest().trim().isEmpty() && session_Configurations.getPortNumber() != 0) {
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET " + containers.get(i).getContainer_key().replaceFirst((i)+"_", "") + " " + 
									containers.get(i).getContainer_value() + ";");
							
							TimeUnit.MILLISECONDS.sleep(500);
							print_writer.println("LAYER6*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET " + containers.get(i).getContainer_key().replaceFirst((i)+"_", "") + " " + 
									containers.get(i).getContainer_value() + ";");
						}
						
					}
					TimeUnit.SECONDS.sleep(1);
					Scene = containers.get(0).getContainer_value().split("/")[ containers.get(0).getContainer_value().split("/").length-1].replace(".sum", "");
					ManualFunctions.Preview(session_selected_sports,Scene, print_writer ,IsGraphicOnScreen);
				}
				
			}else if (request.getRequestURI().contains("uploadFileToManual")) {
				
				 Iterator<String> fileItr = request.getFileNames();
				 
				 if (fileItr.hasNext()) {
					 
					 whichFile = request.getFileMap().entrySet().iterator().next().getKey();
					 
					 if(session_Configurations.getIpAddressEverest().equalsIgnoreCase("localhost") || session_Configurations.getIpAddressScenes().equalsIgnoreCase("")) {
						if(!session_Configurations.getIpAddressEverest().trim().isEmpty() && session_Configurations.getPortNumber() != 0) {
							imgdata.add(new ImageData(whichFile,ManualUtil.MANUAL_MEDIA_DIRECTORY_PATH + 
									"DOAD" + "_" + request.getFileMap().entrySet().iterator().next().getValue().getOriginalFilename()));
						}
						
					}else {
						imgdata.add(new ImageData(whichFile,"//" + session_Configurations.getIpAddressScenes() + "/" + ManualUtil.MANUAL_MEDIA_DIRECTORY_PATH.replace("C:", "c") + 
								"DOAD" + "_" + request.getFileMap().entrySet().iterator().next().getValue().getOriginalFilename()));
					}
					 
					 while (fileItr.hasNext()) {
						 
						mpf = request.getFile(fileItr.next());
						if(session_Configurations.getIpAddressEverest().equalsIgnoreCase("localhost") || session_Configurations.getIpAddressScenes().equalsIgnoreCase("")) {
							if(!session_Configurations.getIpAddressEverest().trim().isEmpty() && session_Configurations.getPortNumber() != 0) {
								file = new File(ManualUtil.MANUAL_MEDIA_DIRECTORY_PATH + 
										"DOAD" + "_" + request.getFileMap().entrySet().iterator().next().getValue().getOriginalFilename());
								mpf.transferTo(file);
							}
							
						}else {
							file = new File("//" + session_Configurations.getIpAddressScenes() + "/" + ManualUtil.MANUAL_MEDIA_DIRECTORY_PATH.replace("C:", "c") + 
									"DOAD" + "_" + request.getFileMap().entrySet().iterator().next().getValue().getOriginalFilename());
							mpf.transferTo(file);
						}
					 }
				  }
		      }
			
		return  objectMapper.writeValueAsString(session_Data);
	}

	@SuppressWarnings("resource")
	@RequestMapping(value = {"/processManualProcedures"}, method={RequestMethod.GET,RequestMethod.POST})    
	public @ResponseBody String processManualProcedures(
			@RequestParam(value = "whatToProcess", required = false, defaultValue = "") String whatToProcess,
			@RequestParam(value = "valueToProcess", required = false, defaultValue = "") String valueToProcess) 
					throws IOException, IllegalAccessException, InvocationTargetException, JAXBException, InterruptedException
	{	
		Map<String, String> json = new HashMap<>();
		
		switch (whatToProcess.toUpperCase()) {
		case "BUILD_CONNECTION":
			print_writer = new PrintWriter(new Socket(session_Configurations.getIpAddressEverest(), 
				session_Configurations.getPortNumber()).getOutputStream(), true);
			return null;
		case "LOAD_SCENE": case "LOAD_DATA": case "CHECK_CONNECTION":case "LOAD_PREVIOUS_SCENE": case "ANIMATE-OUT": case "ANIMATE-IN": case "CLEAR-ALL": case "BADMINTON-OPTIONS": 
		case "READ-DATA-AND-PREVIEW": case "LOAD_CONTAINER": case "PREVIEW":case "MATCH_PREVIEW":case"PREVIEW_IMAGE_DATA": case "PREVIEW-IN":
			switch(whatToProcess.toUpperCase()) {
			case "LOAD_CONTAINER":
				imgdata.clear();
				
				//Delete preview
				new java.io.File("C:/Temp/Preview.png").delete();
				
				is_previous_data = true;
				if(session_Configurations.getIpAddressScenes().equalsIgnoreCase("localhost") || session_Configurations.getIpAddressScenes().equalsIgnoreCase("")) {
					session_Data = (ContainerData)JAXBContext.newInstance(ContainerData.class).createUnmarshaller().unmarshal(
							new File(ManualUtil.MANUAL_DIRECTORY + ManualUtil.DATA_DIRECTORY + valueToProcess));
				}else {
					session_Data = (ContainerData)JAXBContext.newInstance(ContainerData.class).createUnmarshaller().unmarshal(
							new File("//" + session_Configurations.getIpAddressScenes() + "//" + 
									ManualUtil.MANUAL_DIRECTORY.replace("C:", "c") + ManualUtil.DATA_DIRECTORY + valueToProcess));
				}
				
				Collections.sort(session_Data.getContainers());
				
				TimeUnit.SECONDS.sleep(2);
				Scene = session_Data.getContainers().get(0).getContainer_value().split("Scenes/")[1];
				if(session_Configurations.getIpAddressEverest().equalsIgnoreCase("localhost") || session_Configurations.getIpAddressScenes().equalsIgnoreCase("")) {
					if(!session_Configurations.getIpAddressEverest().trim().isEmpty() && session_Configurations.getPortNumber() != 0) {
						new Scene(session_Data.getContainers().get(0).getContainer_value()).
						scene_load(print_writer,session_Data.getContainers().get(0).getContainer_value());
					}
					
				}else {
					new Scene(session_Data.getContainers().get(0).getContainer_value().replace("C:", "c")).
							scene_load(print_writer,session_Data.getContainers().get(0).getContainer_value().replace("C:", "c"));
				}
				ManualFunctions.Preview(session_selected_sports,Scene, print_writer ,IsGraphicOnScreen);
				
				return objectMapper.writeValueAsString(session_Data);
		
			case "READ-DATA-AND-PREVIEW": case "PREVIEW":
				imgdata.clear();
				if(valueToProcess.equalsIgnoreCase("BLANK")) {
					return  objectMapper.writeValueAsString(session_Data);
				}else {
					if(whatToProcess.toUpperCase().equalsIgnoreCase("READ-DATA-AND-PREVIEW")) {
						is_previous_data = true;
					}
					
					if(whatToProcess.toUpperCase().equalsIgnoreCase("PREVIEW")) {
						valueToProcess = valueToProcess.replace(".sum", ".xml");
					}
					//TimeUnit.SECONDS.sleep(3);
					
					if(session_Configurations.getIpAddressScenes().equalsIgnoreCase("localhost") || session_Configurations.getIpAddressScenes().equalsIgnoreCase("")) {
						session_Data = (ContainerData)JAXBContext.newInstance(ContainerData.class).createUnmarshaller().unmarshal(
								new File(ManualUtil.MANUAL_DIRECTORY + ManualUtil.DATA_DIRECTORY + valueToProcess));
					}else {
						session_Data = (ContainerData)JAXBContext.newInstance(ContainerData.class).createUnmarshaller().unmarshal(
								new File("//" + session_Configurations.getIpAddressScenes() + "//" +
										ManualUtil.MANUAL_DIRECTORY.replace("C:", "c") + ManualUtil.DATA_DIRECTORY + valueToProcess));
					}
					
					Collections.sort(session_Data.getContainers());
					
					if(whatToProcess.toUpperCase().equalsIgnoreCase("READ-DATA-AND-PREVIEW")) {
						if(session_Configurations.getIpAddressEverest().equalsIgnoreCase("localhost") || session_Configurations.getIpAddressScenes().equalsIgnoreCase("")) {
							if(!session_Configurations.getIpAddressEverest().trim().isEmpty() && session_Configurations.getPortNumber() != 0) {
								new Scene(session_Data.getContainers().get(0).getContainer_value()).
								scene_load(print_writer,session_Data.getContainers().get(0).getContainer_value());
							}
							
						}else {
							new Scene(session_Data.getContainers().get(0).getContainer_value().replace("C:", "c")).
									scene_load(print_writer,session_Data.getContainers().get(0).getContainer_value().replace("C:", "c"));
						}
					}
					
					for(int i = 1; i < session_Data.getContainers().size() ; i++) {
						if(!session_Configurations.getIpAddressEverest().trim().isEmpty() && session_Configurations.getPortNumber() != 0) {
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET " + session_Data.getContainers().get(i).getContainer_key().replaceFirst((i)+"_", "") + " " + 
									session_Data.getContainers().get(i).getContainer_value() + ";");
							
							TimeUnit.MILLISECONDS.sleep(200);
							print_writer.println("LAYER6*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET " + session_Data.getContainers().get(i).getContainer_key().replaceFirst((i)+"_", "") + " " + 
									session_Data.getContainers().get(i).getContainer_value() + ";");
						}
						
					}
					TimeUnit.SECONDS.sleep(2);
					if(whatToProcess.toUpperCase().equalsIgnoreCase("READ-DATA-AND-PREVIEW")) {
						Scene = session_Data.getContainers().get(0).getContainer_value().split("Scenes/")[1];
					}
					
					if(whatToProcess.toUpperCase().equalsIgnoreCase("PREVIEW")) {
						Scene = valueToProcess.replace(".xml", ".sum");
					}
					
					ManualFunctions.Preview(session_selected_sports,Scene, print_writer ,IsGraphicOnScreen);
					
					return  objectMapper.writeValueAsString(session_Data);
				}
				
			case "LOAD_SCENE":
				is_previous_data = false;
				Scene = valueToProcess;
				if(session_Configurations.getIpAddressEverest().equalsIgnoreCase("localhost") || session_Configurations.getIpAddressScenes().equalsIgnoreCase("")) {
					new Scene(ManualUtil.SCENE_DIRECTORY + ManualUtil.SCENES_DIRECTORY + valueToProcess).
					scene_load(print_writer,ManualUtil.SCENE_DIRECTORY + ManualUtil.SCENES_DIRECTORY + valueToProcess);
				}else {
					new Scene("//" + session_Configurations.getIpAddressScenes() + "//" + ManualUtil.SCENE_DIRECTORY.replace("C:", "c") + 
							ManualUtil.SCENES_DIRECTORY + valueToProcess).scene_load(print_writer,"//" + session_Configurations.getIpAddressScenes() +
									"//" + ManualUtil.SCENE_DIRECTORY.replace("C:", "c") + ManualUtil.SCENES_DIRECTORY + valueToProcess);
				}
				
				ManualFunctions.Preview(session_selected_sports,"", print_writer,false);
				break;
			}
			
			//File file = new File(ManualUtil.MANUAL_DIRECTORY + ManualUtil.CONTAINER_FILE);
			
			switch (whatToProcess.toUpperCase()) {
			case "LOAD_PREVIOUS_SCENE":
				
				if(session_Configurations.getIpAddressScenes().equalsIgnoreCase("localhost") || session_Configurations.getIpAddressScenes().equalsIgnoreCase("")) {
					session_Data = (ContainerData)JAXBContext.newInstance(ContainerData.class).createUnmarshaller().unmarshal(
							new File(ManualUtil.MANUAL_DIRECTORY + ManualUtil.DATA_DIRECTORY + valueToProcess));
				}else {
					session_Data = (ContainerData)JAXBContext.newInstance(ContainerData.class).createUnmarshaller().unmarshal(
							new File("//" + session_Configurations.getIpAddressScenes() + "//" + 
									ManualUtil.MANUAL_DIRECTORY.replace("C:", "c") + ManualUtil.DATA_DIRECTORY + valueToProcess));
				}
				
				Collections.sort(session_Data.getContainers());
				for(int i = 1; i < session_Data.getContainers().size() ; i++) {
//					if()
					print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET " + session_Data.getContainers().get(i).getContainer_key().replaceFirst((i)+"_", "") + " " + 
											session_Data.getContainers().get(i).getContainer_value() + ";");
					
					TimeUnit.MILLISECONDS.sleep(200);
					print_writer.println("LAYER6*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET " + session_Data.getContainers().get(i).getContainer_key().replaceFirst((i)+"_", "") + " " + 
							session_Data.getContainers().get(i).getContainer_value() + ";");
				}
				ManualFunctions.Preview(session_selected_sports,Scene, print_writer,IsGraphicOnScreen);
				return  objectMapper.writeValueAsString(session_Data);
				
			case "LOAD_DATA":
				//Delete preview
//				new java.io.File("C:/Temp/Preview.png").delete();
				
				imgdata.clear();
		        // Check if the file exists
//		        if (file.exists()) {
//		            // Try to delete the file
//		            if (file.delete()) {
//		                System.out.println("File deleted successfully.");
//		            } else {
//		                System.out.println("Failed to delete the file.");
//		            }
//		        }
		        
				if(session_Configurations.getIpAddressScenes().equalsIgnoreCase("localhost") || session_Configurations.getIpAddressScenes().equalsIgnoreCase("")) {
				//Rows and columns with unwanted tags removed
					if(valueToProcess.contains(",")) {
						print_writer.println("LAYER1*EVEREST*STAGE*DIRECTOR*In SHOW 138.0;");
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vRows " +valueToProcess.split(",")[2]+ ";");
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vCoumms " +valueToProcess.split(",")[1]+ ";");
						print_writer.println("LAYER1*EVEREST*GLOBAL TEMPLATE_SAVE_ACTIVE_ONLY " +
								ManualUtil.MANUAL_DIRECTORY + ManualUtil.CONTAINER_FILE + ";");
						print_writer.println("LAYER1*EVEREST*STAGE*DIRECTOR*In SHOW 0.0;");
						
						
						
						TimeUnit.MILLISECONDS.sleep(400);
						
						print_writer.println("LAYER6*EVEREST*STAGE*DIRECTOR*In SHOW 138.0;");
						print_writer.println("LAYER6*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vRows " +valueToProcess.split(",")[2]+ ";");
						print_writer.println("LAYER6*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vCoumms " +valueToProcess.split(",")[1]+ ";");
						print_writer.println("LAYER6*EVEREST*GLOBAL TEMPLATE_SAVE_ACTIVE_ONLY " +
								ManualUtil.MANUAL_DIRECTORY + ManualUtil.CONTAINER_FILE + ";");
						print_writer.println("LAYER6*EVEREST*STAGE*DIRECTOR*In SHOW 0.0;");
					}else {
						print_writer.println("LAYER1*EVEREST*GLOBAL TEMPLATE_SAVE " +
								ManualUtil.MANUAL_DIRECTORY + ManualUtil.CONTAINER_FILE + ";");
					}
					
					//GetDataReturn();
//					print_writer.println("LAYER1*EVEREST*GLOBAL TEMPLATE_SAVE " +
//							ManualUtil.MANUAL_DIRECTORY + ManualUtil.CONTAINER_FILE + ";");
					//print_writer.println("LAYER1*EVEREST*GLOBAL TEMPLATE_GET_ACTIVE_ONLY;");
					TimeUnit.SECONDS.sleep(2);
					boolean exitLoop = false; int numberOfAttempts = 5;
					List<String> allLines = new ArrayList<String>();
					while (exitLoop == false){
						if(new File(ManualUtil.MANUAL_DIRECTORY + ManualUtil.CONTAINER_FILE).exists()) {
							allLines = Files.readAllLines(Paths.get(ManualUtil.MANUAL_DIRECTORY + ManualUtil.CONTAINER_FILE));
							break;
						} else {
							TimeUnit.SECONDS.sleep(1);
							numberOfAttempts = numberOfAttempts - 1;
						}
						if(numberOfAttempts <= 0)
						{
							break;
						}
					}
					return objectMapper.writeValueAsString(allLines);
				}else {
					//Rows and columns with unwanted tags removed
			        // Check if the file exists
//			        if (file.exists()) {
//			            // Try to delete the file
//			            if (file.delete()) {
//			                System.out.println("File deleted successfully.");
//			            } else {
//			                System.out.println("Failed to delete the file.");
//			            }
//			        }
					if(valueToProcess.contains(",")) {
						print_writer.println("LAYER1*EVEREST*STAGE*DIRECTOR*In SHOW 138.0;");
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vRows " +valueToProcess.split(",")[2]+ ";");
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vCoumms " +valueToProcess.split(",")[1]+ ";");
						print_writer.println("LAYER1*EVEREST*GLOBAL TEMPLATE_SAVE_ACTIVE_ONLY " + "//" + session_Configurations.getIpAddressScenes() + "//" + 
								ManualUtil.MANUAL_DIRECTORY.replace("C:", "c") + ManualUtil.CONTAINER_FILE + ";");
						print_writer.println("LAYER1*EVEREST*STAGE*DIRECTOR*In SHOW 0.0;");
						
						TimeUnit.SECONDS.sleep(200);
						
						print_writer.println("LAYER6*EVEREST*STAGE*DIRECTOR*In SHOW 138.0;");
						print_writer.println("LAYER6*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vRows " +valueToProcess.split(",")[2]+ ";");
						print_writer.println("LAYER6*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vCoumms " +valueToProcess.split(",")[1]+ ";");
						print_writer.println("LAYER6*EVEREST*GLOBAL TEMPLATE_SAVE_ACTIVE_ONLY " + "//" + session_Configurations.getIpAddressScenes() + "//" + 
								ManualUtil.MANUAL_DIRECTORY.replace("C:", "c") + ManualUtil.CONTAINER_FILE + ";");
						print_writer.println("LAYER6*EVEREST*STAGE*DIRECTOR*In SHOW 0.0;");
					}else {
						print_writer.println("LAYER1*EVEREST*GLOBAL TEMPLATE_SAVE " + "//" + session_Configurations.getIpAddressScenes() + "//" + 
								ManualUtil.MANUAL_DIRECTORY.replace("C:", "c") + ManualUtil.CONTAINER_FILE + ";");
						
						TimeUnit.SECONDS.sleep(2);
						
						print_writer.println("LAYER6*EVEREST*GLOBAL TEMPLATE_SAVE " + "//" + session_Configurations.getIpAddressScenes() + "//" + 
								ManualUtil.MANUAL_DIRECTORY.replace("C:", "c") + ManualUtil.CONTAINER_FILE + ";");
					}
					
					TimeUnit.SECONDS.sleep(2);
					boolean exitLoop = false; int numberOfAttempts = 5;
					List<String> allLines = new ArrayList<String>();
					while (exitLoop == false){
						if(new File("//" + session_Configurations.getIpAddressScenes() + "//" +
								ManualUtil.MANUAL_DIRECTORY.replace("C:", "c") + ManualUtil.CONTAINER_FILE).exists()) {
							allLines = Files.readAllLines(Paths.get("//" + session_Configurations.getIpAddressScenes() + "//" + 
								ManualUtil.MANUAL_DIRECTORY.replace("C:", "c") + ManualUtil.CONTAINER_FILE));
							break;
						} else {
							TimeUnit.SECONDS.sleep(1);
							numberOfAttempts = numberOfAttempts - 1;
						}
						if(numberOfAttempts <= 0)
						{
							break;
						}
					}
					return objectMapper.writeValueAsString(allLines);
				}
			case "PREVIEW_IMAGE_DATA": case "PREVIEW-IN":
				Path filePath = session_Configurations.getIpAddressEverest().equalsIgnoreCase("LOCALHOST")
			        ? Paths.get("D:\\layer6_snapshot.png")
			        : Paths.get("\\\\" + session_Configurations.getIpAddressEverest() + "\\d\\layer6_snapshot.png");

				if (Files.exists(filePath)) {
				    json.put("file_data", Base64.getEncoder().encodeToString(Files.readAllBytes(filePath)));
				    json.put("content_type", "image/PNG");
				    return objectMapper.writeValueAsString(json);
				}				
//			    JSONObject json = new JSONObject();
//			    Path filePath = session_Configurations.getIpAddressEverest().equalsIgnoreCase("LOCALHOST") 
//			        ? Paths.get("D:\\layer6_snapshot.png") 
//			        : Paths.get("\\\\" + session_Configurations.getIpAddressEverest() + "\\d\\layer6_snapshot.png");
//
//			    if (Files.exists(filePath)) {
//			        json.put("file_data", Base64.getEncoder().encodeToString(Files.readAllBytes(filePath)));
//			        json.put("content_type", "image/PNG");
//			        return json.toString();
//			    }
			    return "Preview Image does not exist."; 			
			}
			switch (whatToProcess.toUpperCase()) {
			
			case "ANIMATE-OUT":
				//print_writer.println("LAYER1*EVEREST*STAGE*DIRECTOR*In CONTINUE_REVERSE;");
				print_writer.println("LAYER1*EVEREST*STAGE*DIRECTOR*Out START;");
				print_writer.println("LAYER1*EVEREST*STAGE*DIRECTOR*In CONTINUE;");
				IsGraphicOnScreen = false;
				return objectMapper.writeValueAsString(null);
			case "ANIMATE-IN":
				print_writer.println("LAYER1*EVEREST*STAGE*DIRECTOR*In START;");
				IsGraphicOnScreen = true;
				return  objectMapper.writeValueAsString(session_Data);
			case "CLEAR-ALL":
				print_writer.println("LAYER1*EVEREST*SINGLE_SCENE CLEAR;");
				IsGraphicOnScreen = false;
				return objectMapper.writeValueAsString(null);
			
			case "CHECK_CONNECTION":
				if(session_Configurations != null) {
					connection = session_Configurations.getIpAddressEverest().equalsIgnoreCase("LOCALHOST")? 
							ManualFunctions.checkConnection("127.0.0.1", session_Configurations.getPortNumber(), 1000): ManualFunctions.checkConnection(session_Configurations.getIpAddressEverest(),
									session_Configurations.getPortNumber(), 1000);
				}
				
				 json.put("connection_type", connection);
			    return json.toString();
			}
		
		default:
			return objectMapper.writeValueAsString(null);
		}
	}
	public static String SetData(List<Container> containers,Boolean is_previous_data,List<ImageData> imgdata,String Key,String Value) throws InterruptedException, NumberFormatException
    {
			if(imgdata.size() == 1) {
				if(!imgdata.get(0).getImageId().equalsIgnoreCase(Key)) {
					if(is_previous_data == true) {
						for(int i = 0;i< session_Data.getContainers().size();i++) {
							if(session_Data.getContainers().get(i).getContainer_key().equalsIgnoreCase(Key)) {
								containers.add(new Container(Integer.valueOf(Key.split("_")[0]), Key, session_Data.getContainers().get(i).getContainer_value()));
								break;
							}
						}
					}else {
						containers.add(new Container(Integer.valueOf(Key.split("_")[0]), Key, Value));
					}
					//break;
				}
			}else if(imgdata.size() == 2) {
				if(!imgdata.get(0).getImageId().equalsIgnoreCase(Key) && !imgdata.get(1).getImageId().equalsIgnoreCase(Key)) {
					if(is_previous_data == true) {
						for(int i = 0;i< session_Data.getContainers().size();i++) {
							if(session_Data.getContainers().get(i).getContainer_key().equalsIgnoreCase(Key)) {
								containers.add(new Container(Integer.valueOf(Key.split("_")[0]), Key, session_Data.getContainers().get(i).getContainer_value()));
								break;
							}
						}
					}else {
						containers.add(new Container(Integer.valueOf(Key.split("_")[0]), Key, Value));
					}
					//break;
				}
			}else if(imgdata.size() == 3) {
				if(!imgdata.get(0).getImageId().equalsIgnoreCase(Key) && !imgdata.get(1).getImageId().equalsIgnoreCase(Key) 
						&& !imgdata.get(2).getImageId().equalsIgnoreCase(Key)) {
					if(is_previous_data == true) {
						for(int i = 0;i< session_Data.getContainers().size();i++) {
							if(session_Data.getContainers().get(i).getContainer_key().equalsIgnoreCase(Key)) {
								containers.add(new Container(Integer.valueOf(Key.split("_")[0]), Key, session_Data.getContainers().get(i).getContainer_value()));
								break;
							}
						}
					}else {
						containers.add(new Container(Integer.valueOf(Key.split("_")[0]), Key, Value));
					}
					//break;
				}
			}else if(imgdata.size() == 4) {
				if(!imgdata.get(0).getImageId().equalsIgnoreCase(Key) && !imgdata.get(1).getImageId().equalsIgnoreCase(Key) 
						&& !imgdata.get(2).getImageId().equalsIgnoreCase(Key) && !imgdata.get(3).getImageId().equalsIgnoreCase(Key)) {
					if(is_previous_data == true) {
						for(int i = 0;i< session_Data.getContainers().size();i++) {
							if(session_Data.getContainers().get(i).getContainer_key().equalsIgnoreCase(Key)) {
								containers.add(new Container(Integer.valueOf(Key.split("_")[0]), Key, session_Data.getContainers().get(i).getContainer_value()));
								break;
							}
						}
					}else {
						containers.add(new Container(Integer.valueOf(Key.split("_")[0]), Key, Value));
					}
					//break;
				}
			}else if(imgdata.size() == 5) {
				if(!imgdata.get(0).getImageId().equalsIgnoreCase(Key) && !imgdata.get(1).getImageId().equalsIgnoreCase(Key) 
						&& !imgdata.get(2).getImageId().equalsIgnoreCase(Key) && !imgdata.get(3).getImageId().equalsIgnoreCase(Key)
						&& !imgdata.get(4).getImageId().equalsIgnoreCase(Key)) {
					if(is_previous_data == true) {
						for(int i = 0;i< session_Data.getContainers().size();i++) {
							if(session_Data.getContainers().get(i).getContainer_key().equalsIgnoreCase(Key)) {
								containers.add(new Container(Integer.valueOf(Key.split("_")[0]), Key, session_Data.getContainers().get(i).getContainer_value()));
								break;
							}
						}
					}else {
						containers.add(new Container(Integer.valueOf(Key.split("_")[0]), Key, Value));
					}
					//break;
				}
			}else if(imgdata.size() == 6) {
				if(!imgdata.get(0).getImageId().equalsIgnoreCase(Key) && !imgdata.get(1).getImageId().equalsIgnoreCase(Key) 
						&& !imgdata.get(2).getImageId().equalsIgnoreCase(Key) && !imgdata.get(3).getImageId().equalsIgnoreCase(Key)
						&& !imgdata.get(4).getImageId().equalsIgnoreCase(Key) && !imgdata.get(5).getImageId().equalsIgnoreCase(Key)) {
					if(is_previous_data == true) {
						for(int i = 0;i< session_Data.getContainers().size();i++) {
							if(session_Data.getContainers().get(i).getContainer_key().equalsIgnoreCase(Key)) {
								containers.add(new Container(Integer.valueOf(Key.split("_")[0]), Key, session_Data.getContainers().get(i).getContainer_value()));
								break;
							}
						}
					}else {
						containers.add(new Container(Integer.valueOf(Key.split("_")[0]), Key, Value));
					}
					//break;
				}
			}else if(imgdata.size() == 7) {
				if(!imgdata.get(0).getImageId().equalsIgnoreCase(Key) && !imgdata.get(1).getImageId().equalsIgnoreCase(Key) 
						&& !imgdata.get(2).getImageId().equalsIgnoreCase(Key) && !imgdata.get(3).getImageId().equalsIgnoreCase(Key)
						&& !imgdata.get(4).getImageId().equalsIgnoreCase(Key) && !imgdata.get(5).getImageId().equalsIgnoreCase(Key)
						&& !imgdata.get(6).getImageId().equalsIgnoreCase(Key)) {
					if(is_previous_data == true) {
						for(int i = 0;i< session_Data.getContainers().size();i++) {
							if(session_Data.getContainers().get(i).getContainer_key().equalsIgnoreCase(Key)) {
								containers.add(new Container(Integer.valueOf(Key.split("_")[0]), Key, session_Data.getContainers().get(i).getContainer_value()));
								break;
							}
						}
					}else {
						containers.add(new Container(Integer.valueOf(Key.split("_")[0]), Key, Value));
					}
					//break;
				}
			}else if(imgdata.size() == 8) {
				if(!imgdata.get(0).getImageId().equalsIgnoreCase(Key) && !imgdata.get(1).getImageId().equalsIgnoreCase(Key) 
						&& !imgdata.get(2).getImageId().equalsIgnoreCase(Key) && !imgdata.get(3).getImageId().equalsIgnoreCase(Key)
						&& !imgdata.get(4).getImageId().equalsIgnoreCase(Key) && !imgdata.get(5).getImageId().equalsIgnoreCase(Key)
						&& !imgdata.get(6).getImageId().equalsIgnoreCase(Key) && !imgdata.get(7).getImageId().equalsIgnoreCase(Key)) {
					if(is_previous_data == true) {
						for(int i = 0;i< session_Data.getContainers().size();i++) {
							if(session_Data.getContainers().get(i).getContainer_key().equalsIgnoreCase(Key)) {
								containers.add(new Container(Integer.valueOf(Key.split("_")[0]), Key, session_Data.getContainers().get(i).getContainer_value()));
								break;
							}
						}
					}else {
						containers.add(new Container(Integer.valueOf(Key.split("_")[0]), Key, Value));
					}
					//break;
				}
			}else if(imgdata.size() == 9) {
				if(!imgdata.get(0).getImageId().equalsIgnoreCase(Key) && !imgdata.get(1).getImageId().equalsIgnoreCase(Key) 
						&& !imgdata.get(2).getImageId().equalsIgnoreCase(Key) && !imgdata.get(3).getImageId().equalsIgnoreCase(Key)
						&& !imgdata.get(4).getImageId().equalsIgnoreCase(Key) && !imgdata.get(5).getImageId().equalsIgnoreCase(Key)
						&& !imgdata.get(6).getImageId().equalsIgnoreCase(Key) && !imgdata.get(7).getImageId().equalsIgnoreCase(Key)
						&& !imgdata.get(8).getImageId().equalsIgnoreCase(Key)) {
					if(is_previous_data == true) {
						for(int i = 0;i< session_Data.getContainers().size();i++) {
							if(session_Data.getContainers().get(i).getContainer_key().equalsIgnoreCase(Key)) {
								containers.add(new Container(Integer.valueOf(Key.split("_")[0]), Key, session_Data.getContainers().get(i).getContainer_value()));
								break;
							}
						}
					}else {
						containers.add(new Container(Integer.valueOf(Key.split("_")[0]), Key, Value));
					}
					//break;
				}
			}else if(imgdata.size() == 10) {
				if(!imgdata.get(0).getImageId().equalsIgnoreCase(Key) && !imgdata.get(1).getImageId().equalsIgnoreCase(Key) 
						&& !imgdata.get(2).getImageId().equalsIgnoreCase(Key) && !imgdata.get(3).getImageId().equalsIgnoreCase(Key)
						&& !imgdata.get(4).getImageId().equalsIgnoreCase(Key) && !imgdata.get(5).getImageId().equalsIgnoreCase(Key)
						&& !imgdata.get(6).getImageId().equalsIgnoreCase(Key) && !imgdata.get(7).getImageId().equalsIgnoreCase(Key)
						&& !imgdata.get(8).getImageId().equalsIgnoreCase(Key) && !imgdata.get(9).getImageId().equalsIgnoreCase(Key)) {
					if(is_previous_data == true) {
						for(int i = 0;i< session_Data.getContainers().size();i++) {
							if(session_Data.getContainers().get(i).getContainer_key().equalsIgnoreCase(Key)) {
								containers.add(new Container(Integer.valueOf(Key.split("_")[0]), Key, session_Data.getContainers().get(i).getContainer_value()));
								break;
							}
						}
					}else {
						containers.add(new Container(Integer.valueOf(Key.split("_")[0]), Key, Value));
					}
					//break;	
				}
			}
		
		return "";
		
    }
	
	public static String getOnlineCurrentDate() throws MalformedURLException, IOException
	{
		HttpURLConnection httpCon = (HttpURLConnection) new URL("https://mail.google.com/").openConnection();
		return new SimpleDateFormat("yyyy-MM-dd").format(new Date(httpCon.getDate()));
	}
}
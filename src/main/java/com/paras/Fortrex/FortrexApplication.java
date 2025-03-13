package com.paras.Fortrex;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.paras.Utils.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.management.ManagementFactory;

@SpringBootApplication
public class FortrexApplication {

	private static Logger logger = LoggerFactory.getLogger(FortrexApplication.class);

	//Why and what is static in java?
	private static int processId;

	public static void main(String[] args) {
		//ManagementFactory.getRuntimeMXBean().getName()
		//42716@C02FR8PEMD6R -> <pid>@<hostname>

		processId = Integer.parseInt(ManagementFactory.getRuntimeMXBean()
				.getName().split("@")[0]);

		if(PlatformUtils.getSupportedOS().equals(PlatformUtils.SUPPORTED_OS.LINUX)) {
			File pidFile = new File("/tmp/fortrex.pid");
			if (pidFile.exists()) {
				pidFile.delete();
			}
			try (FileWriter wrtr = new FileWriter(pidFile)) {
				wrtr.write(Integer.toString(processId));
			} catch (IOException e) {
				System.out.println(e);
			}
		}else{
			System.exit(404);
		}
		SpringApplication.run(FortrexApplication.class, args);

		try {
			String arr = PlatformUtils.executeCommandAndGetStdoutAsString(new String[]{"ls", "/tmp"});
			logger.info("PARAS using logger");
		}catch(Exception ex){

		}
	}

}

/**
 * Copyright 2012-2013 eBay Software Foundation - All Rights Reserved
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *  
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * ============================================================================
 * 
 * @author Benjamin Yarger <byarger@ebay.com>
 * 
 * Class: InstallPackageCommand
 * 
 * Description: 
 * Definition of an install apk command for any given device.
 */

package com.ebay.testdemultiplexer.device.commands;

import com.ebay.testdemultiplexer.connection.TestDevice;
import com.ebay.testdemultiplexer.device.commands.recorder.CommandDeserializer;
import com.ebay.testdemultiplexer.device.commands.recorder.CommandSerializer;
import com.ebay.testdemultiplexer.util.TestDemultiplexerConstants;

public class InstallPackageCommand extends DeviceCommand implements 
	CommandSerializer, CommandDeserializer {

	/** Serialization key identifier. */
	public static final String SERIALIZED_KEY = "INSTALL_PACKAGE_COMMAND";
	
	/** Number of serialized tokens to expect. */
	private static final int NUM_SERIAL_TOKENS = 2;
	
	/** Path to the apk file to install. */
	private String path; 
	
	/**
	 * Default constructor should only be used when deserializing data.
	 */
	public InstallPackageCommand() {
		
	}
	
	/**
	 * Create a new InstallPackageCommand.
	 * @param path Path to the apk install.
	 */
	public InstallPackageCommand(String path) {		
		this.path = path;
	}

	/* (non-Javadoc)
	 * @see com.ebay.testdemultiplexer.device.commands.DeviceCommand#execute(com.ebay.testdemultiplexer.connection.TestDevice)
	 */
	public void execute(TestDevice device) {
		device.getIChimpDevice().installPackage(path);
	}

	/* (non-Javadoc)
	 * @see com.ebay.testdemultiplexer.device.commands.recorder.CommandDeserializer#deserializeCommand(java.lang.String)
	 */
	public boolean deserializeCommand(String data) {
		
		String[] tokens = 
				data.split(TestDemultiplexerConstants.SERIAL_SEPARATOR);
		
		if (tokens.length != NUM_SERIAL_TOKENS) {
			return false;
		} else if (!tokens[0].equals(SERIALIZED_KEY)) {
			return false;
		}
		
		this.path = tokens[1];
		
		return true;
	}

	/* (non-Javadoc)
	 * @see com.ebay.testdemultiplexer.device.commands.recorder.CommandSerializer#serializeCommand()
	 */
	public String serializeCommand() {
		
		String serialized = SERIALIZED_KEY;
		serialized += TestDemultiplexerConstants.SERIAL_SEPARATOR;
		serialized += path;
		
		return serialized;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return SERIALIZED_KEY;
	}
}

package no.so.utils.parser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;

public class ParseFromFile {
	
	/**
	 * Utility function to load Key-value (K,v) pairs from a file. These pairs are separated by a splitter and it is assumed to be the first occcurence of this.
	 * @param path java.nio.file.Path with the path to the file to where to load the K,V pairs
	 * @param splitter - String with splitter expression
	 * @return parsed K,V pairs in a Map
	 */
	public static Map<String,String> getStrKVPairs(Path path, String splitter) {
		Map<String,String> pairs =  new HashMap<>();
		try {
			Files.lines(path).forEach(line -> putKVpair(pairs,line.split(splitter,2))); // limit split to a pair (2 elements) -  the first occcurence of ':' since host ADDR might have ':' 
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return pairs;
	}
	
	private static void putKVpair(Map<String,String> map, String[] splitted) {
		if(splitted.length == 2) {
			map.put(splitted[0], splitted[1].replaceAll("\\s", ""));
		}
	}

	public Map<String,Object> parseConfig (Yaml config, String field){

		return null;
	}

	public static void main (String args[]){

		Map<String,String> confs = ParseFromFile.getStrKVPairs(Path.of("conf/config"),":");

        Map<String,String> creds = ParseFromFile.getStrKVPairs(Path.of("conf/config"),":");

        String host = confs.getOrDefault("host", "localhost");
        int port = Integer.parseInt(confs.getOrDefault("port", "1883"));

	}

}

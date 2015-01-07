package sk.tomsik68.pw.test;

import java.util.ArrayList;
import java.util.HashMap;

import org.junit.Test;

public class TestHashmapOrder {

	@Test
	public void test() {
		// this is used for me to find out how weathers were sorted. It's kinda
		// random :D

		HashMap<String, Object> strings = new HashMap<String, Object>();
		strings.put("clear", null);
		strings.put("rain", null);
		strings.put("storm", null);
		strings.put("hot", null);
		strings.put("meteorstorm", null);
		strings.put("itemrain", null);
		strings.put("arrowrain", null);
		strings.put("sandstorm", null);
		strings.put("godanger", null);
		strings.put("windy", null);
		ArrayList<String> stringList = new ArrayList<String>(strings.keySet());
		for (int i = 0; i < stringList.size(); ++i) {
			System.out.println(String.format("map.put(%d,\"%s\");", i, stringList.get(i)));
		}
	}

}

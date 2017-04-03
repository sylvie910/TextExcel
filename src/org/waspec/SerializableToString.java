package org.waspec;

import java.util.ArrayList;

public interface SerializableToString {

	ArrayList<String> serialize();

	 void deserialize(ArrayList<String> arrayLoaded);
}

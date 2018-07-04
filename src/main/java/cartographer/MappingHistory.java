package cartographer;

import com.google.common.base.Charsets;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

public class MappingHistory {

	public static MappingHistory newMappingsHistory() {
		MappingHistory history = new MappingHistory();
		history.classes = new ArrayList<>();
		history.methods = new ArrayList<>();
		history.fields = new ArrayList<>();
		return history;
	}

	public static MappingHistory readHistory(File file) {
		throw new RuntimeException("I need to code this");
	}

	public List<NamedEntry> classes;
	public List<SignatureEntry> fields;
	public List<SignatureEntry> methods;

	public String generateClassName(String mcVersion) {
		String newClassName = "class_" + classes.size();
		NamedEntry newClassEntry = new NamedEntry(newClassName, mcVersion, Type.CLASS);
		classes.add(newClassEntry);
		return newClassName;
	}

	public String generateMethodName(String signature, String mcVersion) {
		String newMethodName = "method_" + methods.size();
		SignatureEntry newMethodEntry = new SignatureEntry(newMethodName, signature, mcVersion, Type.METHOD);
		methods.add(newMethodEntry);
		return newMethodName;
	}

	public String generateFieldName(String signature, String mcVersion) {
		String newFieldName = "field_" + fields.size();
		SignatureEntry newFieldEntry = new SignatureEntry(newFieldName, signature, mcVersion, Type.FIELD);
		fields.add(newFieldEntry);
		return newFieldName;
	}

	public void writeToFile(File file) throws IOException {
		StringJoiner output = new StringJoiner("\n");
		classes.forEach(namedEntry -> output.add(namedEntry.toString()));
		methods.forEach(namedEntry -> output.add(namedEntry.toString()));
		fields.forEach(namedEntry -> output.add(namedEntry.toString()));
		FileUtils.writeStringToFile(file, output.toString(), Charsets.UTF_8);
	}

	public static class SignatureEntry extends NamedEntry {
		String signature;

		public SignatureEntry(String name, String signature, String tag, Type type) {
			super(name, tag, type);
			this.signature = signature;
		}

		@Override
		public String toString() {
			return type + "\t" + name + "\t" + signature + "\t" + tag;
		}
	}

	public static class NamedEntry extends BaseEntry {
		String name; //name + signature

		String tag; //Minecraft version that the entry was created

		public NamedEntry(String name, String tag, Type type) {
			super(type);
			this.name = name;
			this.tag = tag;
		}

		@Override
		public String toString() {
			return type + "\t" + name + "\t" + tag;
		}
	}

	public static class BaseEntry {
		Type type;

		public BaseEntry(Type type) {
			this.type = type;
		}
	}

	public enum Type {
		CLASS,
		METHOD,
		FIELD
	}

}
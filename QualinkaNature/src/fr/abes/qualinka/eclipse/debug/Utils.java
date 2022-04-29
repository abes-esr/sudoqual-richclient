package fr.abes.qualinka.eclipse.debug;

public class Utils {

	private Utils() {
	}

	public static String createInputPart(Iterable<String> keySet) {
		StringBuilder sb = new StringBuilder();
		boolean isFirst = true;
		for (String key : keySet) {
			if (isFirst) {
				isFirst = false;
			} else {
				sb.append(",\n");
			}
			sb.append("        \"").append(key).append("\": ?");
		}
		return sb.toString();
	}

}

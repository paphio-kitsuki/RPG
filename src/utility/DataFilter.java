package utility;

import java.io.File;
import java.io.FilenameFilter;

public class DataFilter implements FilenameFilter {

	@Override
	public boolean accept(File dir, String name) {
		//if (name.startsWith(Data.filename)) {
			if (name.endsWith(Data.extension)) {
				return true;
			}
		//}
		return false;
	}

}

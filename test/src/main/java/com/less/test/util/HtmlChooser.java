package com.less.test.util;

import com.less.aspider.util.L;
import com.less.test.AppConfig;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class HtmlChooser {

	File file = new File(AppConfig.FILE_CHOOSER);

	List<String> lines = null;

	boolean open = false;

	public HtmlChooser() {
		init();
		check();
	}

	private void init() {
		try {
			lines = FileUtils.readLines(file, "UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void check(){
		this.open = file.exists();
		if(open){
			L.d("=======> choose open <=======");
		} else {
			L.d("=======> choose close <=======");
		}
	}

	public boolean contains(String html){
		if(!open){
			return true;
		}
		for(String line : lines){
			if(html.contains(line)){
				return true;
			}
		}
		return false;
	}
}
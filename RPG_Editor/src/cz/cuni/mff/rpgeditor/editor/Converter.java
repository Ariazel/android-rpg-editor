package cz.cuni.mff.rpgeditor.editor;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DefaultLogger;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.ProjectHelper;


/**
 * Trida, ktera vytvori RPG podle vznikle hry v editoru.
 * Tato trida je staticka, nelze tvorit jeji instance.
 */
public class Converter
{
	private Converter()
	{	}
	
	static void convert(Game g)
	{
		String apk_filepath = getDestinationFilePath();
		editBuildFileDest(apk_filepath);
		
		File ant_build_file = new File(g.game_filepath + "/build.xml");
		
		Project p = new Project();
	    p.setUserProperty("ant.file", ant_build_file.getAbsolutePath());
	    DefaultLogger console_logger = new DefaultLogger();
	    console_logger.setErrorPrintStream(System.err);
	    console_logger.setOutputPrintStream(System.out);
	    console_logger.setMessageOutputLevel(Project.MSG_INFO);
	    p.addBuildListener(console_logger);
	    BuildException ex = null;
	    try 
	    {
	        p.fireBuildStarted();
	        p.init();
	        ProjectHelper helper = ProjectHelper.getProjectHelper();
	        p.addReference("ant.projectHelper", helper);
	        helper.parse(p, ant_build_file);
	        p.executeTarget("clean");
	        p.executeTarget("release");
	    }
	    catch (BuildException e)
	    {
	        ex = e;
	    }
	    finally
	    {
	        p.fireBuildFinished(ex);
	    }
	}
	
	/**
	 * @return Cesta, do ktere ma byt ulozen apk soubor.
	 */
	private static String getDestinationFilePath()
	{
		JFileChooser fc = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("APK file", "apk");
		fc.setFileFilter(filter);
		
		int return_val = fc.showSaveDialog(Main.gui.mapTabbedPane);
		
		if (return_val == JFileChooser.APPROVE_OPTION)
		{
			File file = fc.getSelectedFile();
			return file.getAbsolutePath();
		}
		else
		{
			return null;
		}
	}
	
	/**
	 * Upravi build.xml, aby se vysledny apk soubor ulozil do vybrane slozky.
	 * @param dest Cesta k apk souboru.
	 */
	private static void editBuildFileDest(String dest)
	{
		
	}
}

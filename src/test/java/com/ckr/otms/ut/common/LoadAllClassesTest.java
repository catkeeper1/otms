package com.ckr.otms.ut.common;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * Created by Administrator on 2016/11/26.
 */
public class LoadAllClassesTest {

    private static Logger LOG = LoggerFactory.getLogger(LoadAllClassesTest.class);


    /*
      Jmockit coverage only generate coverage report for the classes have been loaded. To make sure all production
      java classes are included in the report, we use this test case to load all production classes.
      If this class is not executed in surefire maven plugin, please do pass -Dbasedir = {the project folder}
     */
    @Test
    public void loadAllClassedShouldBeTested() {
        loadClassesUnderAFolder(getSrcFolder(), null);
    }

    private String getNextPackageName(String packageName, File subFolder){
        return packageName == null ? subFolder.getName(): packageName +"." + subFolder.getName();
    }

    private void loadClassesUnderAFolder(File curFolder, String packageName) {

        File[] fileList = curFolder.listFiles();

        for (int i = 0; i < fileList.length; i++){
            File scanedFile = fileList[i];

            if(scanedFile.isDirectory()){

                loadClassesUnderAFolder(scanedFile, getNextPackageName(packageName, scanedFile));

            } else if(scanedFile.getName().toLowerCase().endsWith(".java")){

                String className = scanedFile.getName();
                className = className.substring(0, className.length() - 5);
                className = packageName == null ? className: packageName +"." + className;

                try {
                    getClass().getClassLoader().loadClass(className);
                    LOG.info("load class:" + className);
                }catch(ClassNotFoundException e){
                    //do nothing because this may be an annotation but not a class.
                }
            }

        }


        return;
    }

    private File getSrcFolder(){

        File srcDir = new File(getBaseFolder(),"src/main/java");
        if(!srcDir.isDirectory()){
            throw new RuntimeException("invalid basedir:" + srcDir.getAbsolutePath());
        }

        return srcDir;
    }

    private File getBaseFolder(){

        String baseDir = System.getProperty("basedir");

        File baseFolder = new File(baseDir);

        if(!baseFolder.isDirectory()){
            throw new RuntimeException("invalid basedir:" + baseDir);
        }

        return baseFolder;
    }
}

package com.gis.trans.utils;

import com.gis.trans.model.Result;

import java.io.*;
import java.util.jar.JarOutputStream;
import java.util.zip.*;

public class ZipUtils {

    private static final int  BUFFER_SIZE = 2 * 1024;

    /**
     * 将存放在sourceFilePath目录下的源文件，打包成fileName名称的zip文件，并存放到zipFilePath路径下
     * @param sourceFilePath :待压缩的文件路径
     * @param zipFilePath :压缩后存放路径
     * @param fileName :压缩后文件的名称
     * @return
     */
    public static Result fileToZip(String sourceFilePath, String zipFilePath, String fileName){
        Result result = new Result();
        result.setFlag(false);
        File sourceFile = new File(sourceFilePath);
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        FileOutputStream fos = null;
        ZipOutputStream zos = null;

        if(sourceFile.exists() == false){
            System.out.println("待压缩的文件目录："+sourceFilePath+"不存在.");
        }else{
            try {
                File zipFile = new File(zipFilePath + "/" + fileName +".zip");
                boolean delete = false;
                if(sourceFile.exists() == false){
                    System.out.println("待压缩的文件目录："+sourceFilePath+"不存在.");
                }else{
                    File[] sourceFiles = sourceFile.listFiles();
                    if(null == sourceFiles || sourceFiles.length<1){
                        System.out.println("待压缩的文件目录：" + sourceFilePath + "里面不存在文件，无需压缩.");
                    }else{
                        fos = new FileOutputStream(zipFile);
                        zos = new ZipOutputStream(new BufferedOutputStream(fos));
                        byte[] bufs = new byte[1024*50];
                        for(int i=0;i<sourceFiles.length;i++){
                            //创建ZIP实体，并添加进压缩包
                            ZipEntry zipEntry = new ZipEntry(sourceFiles[i].getName());
                            zos.putNextEntry(zipEntry);
                            //读取待压缩的文件并写进压缩包里
                            fis = new FileInputStream(sourceFiles[i]);
                            bis = new BufferedInputStream(fis, 1024*50);
                            int read = 0;
                            while((read=bis.read(bufs, 0, 1024*50)) != -1){
                                zos.write(bufs,0,read);
                            }
                        }
                        result.setFlag(true);
                        result.setData(zipFile.getAbsolutePath());
                    }
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            } finally{
                //关闭流
                try {
                    if(null != bis) bis.close();
                    if(null != zos) zos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
            }
        }
        return result;
    }

    public static void compress(CompressType type) {
        if (type == CompressType.ZIP) {
            zip("压缩", "压缩.zip", CompressType.ZIP);
        } else if (type == CompressType.JAR) {
            zip("压缩", "压缩.jar", CompressType.JAR);
        }
    }

    public static void zip(String inputFile, String outputFile, CompressType type) {
        zip(new File(inputFile), new File(outputFile), type);
    }

    /**
     * 初始化压缩包信息并开始进行压缩
     *
     * @param inputFile  需要压缩的文件或文件夹
     * @param outputFile 压缩后的文件
     * @param type       压缩类型
     */
    public static void zip(File inputFile, File outputFile, CompressType type) {
        ZipOutputStream zos = null;
        try {
            if (type == CompressType.ZIP) {
                zos = new ZipOutputStream(new FileOutputStream(outputFile));
            } else if (type == CompressType.JAR) {
                zos = new JarOutputStream(new FileOutputStream(outputFile));
            } else {
                zos = new ZipOutputStream(new FileOutputStream(outputFile));
            }
            // 设置压缩包注释
            zos.setComment(" 插件数据文件 ");
            zipFile(zos, inputFile, null);
            System.err.println("压缩完成!");
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("压缩失败!");
        } finally {
            if (zos != null) {
                try {
                    zos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 如果是单个文件，那么就直接进行压缩。如果是文件夹，那么递归压缩所有文件夹里的文件
     *
     * @param zos       压缩输出流
     * @param inputFile 需要压缩的文件
     * @param path      需要压缩的文件在压缩包里的路径
     */
    public static void zipFile(ZipOutputStream zos, File inputFile, String path) {
        if (inputFile.isDirectory()) {
            // 记录压缩包中文件的全路径
            String p = null;
            File[] fileList = inputFile.listFiles();
            for (int i = 0; i < fileList.length; i++) {
                File file = fileList[i];
                // 如果路径为空，说明是根目录
                if (path == null || path.isEmpty()) {
                    p = file.getName();
                } else {
                    p = path + File.separator + file.getName();
                }
                // 打印路径
                System.out.println(p);
                // 如果是目录递归调用，直到遇到文件为止
                zipFile(zos, file, p);
            }
        } else {
            zipSingleFile(zos, inputFile, path);
        }
    }

    /**
     * 压缩单个文件到指定压缩流里
     *
     * @param zos       压缩输出流
     * @param inputFile 需要压缩的文件
     * @param path      需要压缩的文件在压缩包里的路径
     * @throws FileNotFoundException
     */
    public static void zipSingleFile(ZipOutputStream zos, File inputFile, String path) {
        try {
            InputStream in = new FileInputStream(inputFile);
            zos.putNextEntry(new ZipEntry(path));
            write(in, zos);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 解压压缩包到指定目录
     *
     * @param inputFile
     * @param outputFile
     */
    public static void unZip(String inputFile, String outputFile) {
        unZip(new File(inputFile), new File(outputFile));
    }

    /**
     * 解压压缩包到指定目录
     *
     * @param inputFile
     * @param outputFile
     */
    public static void unZip(File inputFile, File outputFile) {
        if (!outputFile.exists()) {
            outputFile.mkdirs();
        }

        ZipFile zipFile = null;
        ZipInputStream zipInput = null;
        ZipEntry entry = null;
        OutputStream output = null;
        InputStream input = null;
        File file = null;
        try {
            zipFile = new ZipFile(inputFile);
            zipInput = new ZipInputStream(new FileInputStream(inputFile));
            String path = outputFile.getAbsolutePath() + File.separator;
            while ((entry = zipInput.getNextEntry()) != null) {
                // 从压缩文件里获取指定已压缩文件的输入流
                input = zipFile.getInputStream(entry);

                // 拼装压缩后真实文件路径
                String fileName = path + entry.getName();
                System.out.println(fileName);

                // 创建文件缺失的目录（不然会报异常：找不到指定文件）fileName.substring(0, fileName.lastIndexOf(File.separator))
                file = new File(fileName);

                if(entry.isDirectory()) {
                    file.mkdirs();
                }
                output = new FileOutputStream(fileName);
                // 写出解压后文件数据
                write(input, output);
                output.close();
            }
        } catch (ZipException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (output != null) {
                    output.close();
                }

                if (zipInput != null) {
                    zipInput.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 从输入流写入到输出流的方便方法 【注意】这个函数只会关闭输入流，且读写完成后会调用输出流的flush()函数，但不会关闭输出流！
     *
     * @param input
     * @param output
     */
    private static void write(InputStream input, OutputStream output) {
        int len = -1;
        byte[] buff = new byte[9024];
        try {
            while ((len = input.read(buff)) != -1) {
                output.write(buff, 0, len);
            }
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                input.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



    public static void toZip(String srcDir, OutputStream out, boolean KeepDirStructure)
            throws RuntimeException{

        long start = System.currentTimeMillis();
        ZipOutputStream zos = null ;
        try {
            zos = new ZipOutputStream(out);
            File sourceFile = new File(srcDir);
            compress(sourceFile,zos,sourceFile.getName(),KeepDirStructure);
            long end = System.currentTimeMillis();
            System.out.println("压缩完成，耗时：" + (end - start) +" ms");
        } catch (Exception e) {
            throw new RuntimeException("zip error from ZipUtils",e);
        }finally{
            if(zos != null){
                try {
                    zos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /**
     * 递归压缩方法
     * @param sourceFile 源文件
     * @param zos        zip输出流
     * @param name       压缩后的名称
     * @param KeepDirStructure  是否保留原来的目录结构,true:保留目录结构;
     *                          false:所有文件跑到压缩包根目录下(注意：不保留目录结构可能会出现同名文件,会压缩失败)
     * @throws Exception
     */
    private static void compress(File sourceFile, ZipOutputStream zos, String name,
                                 boolean KeepDirStructure) throws Exception{
        byte[] buf = new byte[BUFFER_SIZE];
        if(sourceFile.isFile()){
            // 向zip输出流中添加一个zip实体，构造器中name为zip实体的文件的名字
            zos.putNextEntry(new ZipEntry(name));
            // copy文件到zip输出流中
            int len;
            FileInputStream in = new FileInputStream(sourceFile);
            while ((len = in.read(buf)) != -1){
                zos.write(buf, 0, len);
            }
            // Complete the entry
            zos.closeEntry();
            in.close();
        } else {
            //是文件夹
            if(KeepDirStructure){
                zos.putNextEntry(new ZipEntry(name + "/"));
            }
            File[] listFiles = sourceFile.listFiles();
            if(listFiles == null || listFiles.length == 0){
                // 需要保留原来的文件结构时,需要对空文件夹进行处理
//                if(KeepDirStructure){
//                    // 空文件夹的处理
//                    zos.putNextEntry(new ZipEntry(name + "/"));
                    // 没有文件，不需要文件的copy
                    zos.closeEntry();
//                }
            }else {
                for (File file : listFiles) {
                    // 判断是否需要保留原来的文件结构
                    if (KeepDirStructure) {
                        // 注意：file.getName()前面需要带上父文件夹的名字加一斜杠,
                        // 不然最后压缩包中就不能保留原来的文件结构,即：所有文件都跑到压缩包根目录下了
                        compress(file, zos, name + "/" + file.getName(),KeepDirStructure);
                    } else {
                        compress(file, zos, file.getName(),KeepDirStructure);
                    }

                }
            }
        }
    }
}

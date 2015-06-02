package edu.ub.sd.onlinereslib.controllers;

import edu.ub.sd.onlinereslib.beans.Resource;
import edu.ub.sd.onlinereslib.services.ResourceStore;
import edu.ub.sd.onlinereslib.webframework.WebController;
import edu.ub.sd.onlinereslib.webframework.annotations.*;

import javax.servlet.ServletOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;

@UrlPathController(path = "/protegit/descarrega/{id}")
public class DownloadResourceController extends WebController {

    @RequireService
    public ResourceStore resourceStore;

    @HttpMethod(type = HttpMethodType.GET)
    public void downloadResource(@HttpRequestParameter(name = "id", fromUrl = true) String id) throws Exception {
        // log(String.format("id [%s]", id));
        Resource resource = resourceStore.getUserBoughtResource(request, id);

        int length;
        File file = resource.getFile();

        ServletOutputStream outStream = response.getOutputStream();
        response.setContentLength((int) file.length());
        response.setHeader("Content-Type", resource.getMimeType());
        response.setHeader("Content-Length", "" + file.length());

        // Instead of always delivering content as an attachment,
        // let the browser decide what to do with the file sent
        response.setHeader("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"");

        byte[] byteBuffer = new byte[8024];
        DataInputStream in = new DataInputStream(new FileInputStream(file));

        // @TODO Check if there's any problem with APR error.
        //       when sending the file (maybe it's a Windows
        //       thing, or could be something about the buffer size...)
        while ((length = in.read(byteBuffer)) != -1) {
            outStream.write(byteBuffer, 0, length);
        }

        in.close();
        outStream.close();
    }

    @HttpMethod(type = HttpMethodType.GET, action = "stream")
    public void streamResource(@HttpRequestParameter(name = "id", fromUrl = true) String id) throws Exception {
        // log(String.format("id [%s]", id));
        Resource resource = resourceStore.getUserBoughtResource(request, id);

        int length;
        File file = resource.getFile();

        ServletOutputStream outStream = response.getOutputStream();
        response.setContentLength((int) file.length());
        response.setHeader("Content-Type", resource.getMimeType());
        response.setHeader("Content-Length", "" + file.length());

        // Instead of always delivering content as an attachment,
        // let the browser decide what to do with the file sent
        //response.setHeader("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"");

        byte[] byteBuffer = new byte[8024];
        DataInputStream in = new DataInputStream(new FileInputStream(file));

        // @TODO Check if there's any problem with APR error.
        //       when sending the file (maybe it's a Windows
        //       thing, or could be something about the buffer size...)
        while ((length = in.read(byteBuffer)) != -1) {
            outStream.write(byteBuffer, 0, length);
        }

        in.close();
        outStream.close();
    }

}

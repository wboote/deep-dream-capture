// Copyright 2015 Qvalent Pty. Ltd.
package deepdream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import com.github.sarxos.webcam.Webcam;

/**
 * @author warwickb
 *
 */
public class Main
{

    /**
     * @param args
     */
    public static void main( final String[] args )
    {
        while (true)
        {
            try
            {
                // capture the webcam image to memory
                final ByteArrayOutputStream baos = new ByteArrayOutputStream();
                final Webcam webcam = Webcam.getDefault();
                webcam.open();
                ImageIO.write(webcam.getImage(), "JPG", baos);

                //post the captured image
                final CloseableHttpClient httpClient = HttpClients.createDefault();
                final HttpPost uploadFile = new HttpPost("http://localhost:8080/DeepDream/Upload");

                final ByteArrayInputStream bais = new ByteArrayInputStream( baos.toByteArray() );
                final MultipartEntityBuilder builder = MultipartEntityBuilder.create();
                builder.addTextBody("field1", "yes", ContentType.TEXT_PLAIN);
                builder.addBinaryBody("file", bais, ContentType.APPLICATION_OCTET_STREAM, "capture.jpg");
                final HttpEntity multipart = builder.build();

                uploadFile.setEntity(multipart);

                final CloseableHttpResponse response = httpClient.execute(uploadFile);
                final HttpEntity responseEntity = response.getEntity();


                Thread.sleep( 10000 );
            }
            catch ( final InterruptedException | IOException e )
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }

}

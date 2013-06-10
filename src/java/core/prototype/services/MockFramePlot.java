package core.prototype.services;

import core.prototype.Utils;
import core.prototype.config.Application;
import javax.ws.rs.Encoded;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/plot-frame-class")
@Encoded
public class MockFramePlot {

    private static final Logger logger = LoggerFactory.getLogger(MockFramePlot.class);
    private static final String rScriptName = "frame-data-plot.R";
    private static final String imageName = "frame-data-plot.jpg";

    @GET
    @Produces({"text/html;charset=UTF-8"})
    @Path("/plot")
    public Response getPlot() {
        Response rs;
        try {
            Application app = Application.getInstance();
            byte[] imageData = app.getDomain().plotMockDataset(imageName, rScriptName);
            String encoded = new sun.misc.BASE64Encoder().encode(imageData);
            rs = Response.ok(encoded).type(MediaType.APPLICATION_OCTET_STREAM)
                    .build();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).
                    entity(Utils.GENERAL_SERVER_ERROR_MESSAGE).
                    build();
        }
        return rs;
    }
}

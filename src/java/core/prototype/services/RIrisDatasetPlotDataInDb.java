package core.prototype.services;

import core.prototype.Utils;
import core.prototype.config.Application;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.Encoded;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/plot-iris-class-db")
@Encoded
public class RIrisDatasetPlotDataInDb {

    private static final Logger logger = LoggerFactory.getLogger(RIrisDatasetPlotDataInDb.class);
    private static final String rScriptName = "iris-data-plot.R";
    private static final String imageName = "iris.jpg";

    @GET
    @Produces({"text/html;charset=UTF-8"})
    @Path("/plot")
    public Response getPlot(
            @DefaultValue("p") @QueryParam("plot-type") @Encoded String plotType) {
        Response rs;
        try {
            Application app = Application.getInstance();
            byte[] imageData = app.getDomain().plotRIrisDataset(imageName, rScriptName);
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

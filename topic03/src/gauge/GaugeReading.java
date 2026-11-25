package gauge;

/*
 * Program 3.5: GaugeReading.java
 *
 * Program description:
 *   An immutable record holding one river-level reading. It implements
 *   Serializable so that whole objects can travel through an object stream.
 *
 * What it shows:
 *   The class shared by both sides of an object-based exchange. The client and
 *   the server must have the same version of this class.
 */
import java.io.Serializable;

/**
 * One telemetry reading.
 *
 * @param stationId   identifier of the gauge station
 * @param epochSecond time of the reading, in seconds since 1970-01-01 UTC
 * @param levelMetres water level in metres
 */
public record GaugeReading(String stationId, long epochSecond, double levelMetres)
        implements Serializable {
	
}
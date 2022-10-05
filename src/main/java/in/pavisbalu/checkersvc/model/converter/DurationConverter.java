package in.pavisbalu.checkersvc.model.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.time.Duration;
import java.time.temporal.ChronoUnit;

/*
 * Adapted from https://thorben-janssen.com/jpa-tips-map-duration-attribute/
 */
@Converter(autoApply = true)
public class DurationConverter implements AttributeConverter<Duration, Long> {

    @Override
    public Long convertToDatabaseColumn(Duration attribute) {
        return attribute.getSeconds();
    }

    @Override
    public Duration convertToEntityAttribute(Long duration) {
        return Duration.of(duration, ChronoUnit.SECONDS);
    }
}

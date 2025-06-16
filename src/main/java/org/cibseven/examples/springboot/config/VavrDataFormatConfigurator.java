package org.cibseven.examples.springboot.config;

import spinjar.com.fasterxml.jackson.databind.ObjectMapper;
import io.vavr.jackson.datatype.VavrModule;
import org.cibseven.spin.spi.DataFormatConfigurator;
import org.cibseven.spin.impl.json.jackson.format.JacksonJsonDataFormat;
import org.cibseven.spin.spi.DataFormat;

// based on https://docs.cibseven.org/manual/latest/reference/spin/extending-spin/#configuring-data-formats
public class VavrDataFormatConfigurator implements DataFormatConfigurator {

    @Override
    public Class<?> getDataFormatClass() {
        return JacksonJsonDataFormat.class; // target JSON data format
    }

    @Override
    public void configure(DataFormat dataFormat) {
        JacksonJsonDataFormat jacksonFormat = (JacksonJsonDataFormat) dataFormat;
        ObjectMapper mapper = jacksonFormat.getObjectMapper();
        mapper.registerModule(new VavrModule()); // register Vavr support
    }
}


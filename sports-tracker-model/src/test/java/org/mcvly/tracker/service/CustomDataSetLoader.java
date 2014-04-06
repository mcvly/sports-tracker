package org.mcvly.tracker.service;

import com.github.springtestdbunit.dataset.AbstractDataSetLoader;
import org.dbunit.dataset.CompositeDataSet;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.csv.CsvDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.File;

/**
 * @author mcvly
 * @since 01.04.14
 */
public class CustomDataSetLoader extends AbstractDataSetLoader {

    private Resource initDataSet = new ClassPathResource("/initDataSet.xml");


    @Override
    protected IDataSet createDataSet(Resource resource) throws Exception {
        return new CompositeDataSet(
                new CsvDataSet(resource.getFile()),
                new FlatXmlDataSetBuilder().build(initDataSet.getInputStream())
        );
    }

}

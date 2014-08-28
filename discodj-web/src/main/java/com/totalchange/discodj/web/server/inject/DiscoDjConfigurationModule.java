package com.totalchange.discodj.web.server.inject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;

public class DiscoDjConfigurationModule extends AbstractModule {
    public static final String CATALOGUE_ROOT = "catalogueRoot";
    public static final String SOLR_HOME = "solrHome";

    private static final Logger logger = LoggerFactory
            .getLogger(DiscoDjConfigurationModule.class);

    private File findFirstFile(String... filenames) {
        for (String filename : filenames) {
            File file = new File(filename);
            if (file.exists() && file.isFile()) {
                return file;
            }
        }
        return null;
    }

    private File findFirstDir(String... filenames) {
        for (String filename : filenames) {
            File file = new File(filename);
            if (file.exists() && file.isDirectory()) {
                return file;
            }
        }
        return null;
    }

    private File findConfigFile() {
        return findFirstFile("/etc/discodj.properties");
    }

    private String defaultCatalogueLocation() throws IOException {
        File dir = findFirstDir("/media", "/mnt");
        if (dir == null) {
            return "no catalogue directory found - check your "
                    + "configuration :-(";
        } else {
            return dir.getCanonicalPath();
        }
    }

    private String defaultSolrHomeLocation() throws IOException {
        File file = findFirstFile("/var/discodj/solr/solr.xml");
        if (file == null) {
            return "no SOLR home directory found - check your "
                    + "configuration :-(";
        } else {
            return file.getParentFile().getCanonicalPath();
        }
    }

    private Properties getConfiguration() throws IOException {
        logger.trace("Figuring out configuration");
        Properties props = new Properties();

        File configFile = findConfigFile();
        if (configFile != null) {
            FileInputStream in = new FileInputStream(configFile);
            try {
                props.load(in);
            } finally {
                in.close();
            }
        }

        if (props.getProperty(CATALOGUE_ROOT) == null) {
            props.put(CATALOGUE_ROOT, defaultCatalogueLocation());
        }

        if (props.getProperty(SOLR_HOME) == null) {
            props.put(SOLR_HOME, defaultSolrHomeLocation());
        }

        logger.trace("Configuration set to {}", props);
        return props;
    }

    @Override
    protected void configure() {
        try {
            Names.bindProperties(binder(), getConfiguration());
        } catch (IOException ioEx) {
            logger.error("Failed to load configuration", ioEx);
            throw new RuntimeException(ioEx);
        }
    }
}

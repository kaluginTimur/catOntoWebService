/*
 * Copyright (C) 2018 Timur Kalugin
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.tika.catontowebservice.semantic.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;

/**
 *
 * @author Timur Kalugin
 */
public class OntologyUtil {
    
    private static final String ONTOLOGY_RESOURCE_PATH = "semantic/ontology/";
    private static final String ONTOLOGY_FILE_NAME = "catonto.owl";
    private static final String ONTOLOGY_PATH = ONTOLOGY_RESOURCE_PATH + ONTOLOGY_FILE_NAME;
    
    private static final OWLOntology ontology;
    
    static {
        OWLOntologyManager man = OWLManager.createOWLOntologyManager();
        try {
            File ontologyFile = getOntologyFile();
            ontology = man.loadOntologyFromOntologyDocument(ontologyFile);
        } catch (OWLOntologyCreationException | FileNotFoundException ex) {
            System.err.println("Initial OWL ontology load failed. " + ex);  
            throw new ExceptionInInitializerError(ex);
        }
    }
    
    public static OWLOntology getOntology() {
        return ontology;
    }
    
    private static File getOntologyFile() throws FileNotFoundException {
        ClassLoader classLoader = OntologyUtil.class.getClassLoader();
        URL fileUrl = classLoader.getResource(ONTOLOGY_PATH);
        if(fileUrl == null) throw new FileNotFoundException("Ontology file not found.");
        File file = new File(fileUrl.getFile());
        return file;
    }
    
}

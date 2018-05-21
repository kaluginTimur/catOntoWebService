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
import org.semanticweb.HermiT.ReasonerFactory;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;

/**
 *
 * @author Timur Kalugin
 */
public class OntologyUtil {
    
    private static final String ONTOLOGY_PROJECT_RESOURCE_PATH = "src/main/resources/semantic/ontology/";
    private static final String ONTOLOGY_RESOURCE_PATH = "semantic/ontology/";
    private static final String ONTOLOGY_FILE_NAME = "catonto.owl";
    private static final File ONTOLOGY_FILE;
    
    private static final OWLOntology ontology;
    private static final OWLReasoner reasoner;
    
    static {
        OWLOntologyManager man = OWLManager.createOWLOntologyManager();
        try {
            ONTOLOGY_FILE = getOntologyFile();
            ontology = man.loadOntologyFromOntologyDocument(ONTOLOGY_FILE);
            OWLReasonerFactory rf = new ReasonerFactory();
            reasoner = rf.createReasoner(ontology); 
        } catch (OWLOntologyCreationException | FileNotFoundException ex) {
            System.err.println("Initial OWL ontology load failed. " + ex);  
            throw new ExceptionInInitializerError(ex);
        }
    }
    
    public static OWLOntology getOntology() {
        return ontology;
    }
    
    public static OWLReasoner getReasoner() {
        return reasoner;
    }
    
    public static void saveOntology() throws OWLOntologyStorageException {
        ontology.getOWLOntologyManager().saveOntology(ontology);
    }
    
    private static File getOntologyFile() throws FileNotFoundException {
        ClassLoader classLoader = OntologyUtil.class.getClassLoader();
        File file = new File(ONTOLOGY_PROJECT_RESOURCE_PATH + ONTOLOGY_FILE_NAME);
        URL fileUrl = classLoader.getResource(ONTOLOGY_RESOURCE_PATH + ONTOLOGY_FILE_NAME);
        if (!file.exists() && fileUrl == null) {
            throw new FileNotFoundException("Ontology file not found.");
        } else if (file.exists()) {
            return file;
        } else {
            return new File(fileUrl.getPath());
        }
    }
    
}

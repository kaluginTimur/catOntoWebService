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
package com.tika.catontowebservice.web.controllers;

import com.tika.catontowebservice.web.service.OntologyWebService;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Timur Kalugin
 */

@Controller
@RequestMapping(value = "/")
public class MainController {
    
    @Autowired
    private HttpServletRequest request;
    
    @RequestMapping(value = "/breed/{breedName}", method = RequestMethod.GET)
    private ModelAndView getBreedParams(@PathVariable("breedName") String breedName) {
        return new ModelAndView("breedInfo", "breedBodyProp", OntologyWebService.getBodyBreedCharacteristics(breedName))
                .addObject("breedPersonalityProp", OntologyWebService.getPersonalityBreedCharacteristics(breedName))
                .addObject("breedImages", OntologyWebService.getImagePathList(breedName))
                .addObject("breedOrigin", OntologyWebService.getOrigin(breedName))
                .addObject("breedName", breedName);
    }
    
    @RequestMapping(value = "/breeds", method = RequestMethod.GET)
    private ModelAndView getBreeds() {
        return new ModelAndView("breeds", "breedList", OntologyWebService.getBreeds());
    }
    
    @RequestMapping(method = RequestMethod.GET)
    private ModelAndView getProperties() {
        return new ModelAndView("index", "breedBodyProperties", OntologyWebService.getBodyBreedCharacteristics())
                .addObject("breedPersonProperties", OntologyWebService.getPersonalityBreedCharacteristics());
    }
    
    @RequestMapping(value = "/findBreed", method = RequestMethod.POST)
    private ModelAndView findBreeds() {
        return new ModelAndView("index", "breedsFinded", OntologyWebService.findBreeds(request.getParameterMap()))
                .addObject("breedPersonProperties", OntologyWebService.getPersonalityBreedCharacteristics())
                .addObject("breedBodyProperties", OntologyWebService.getBodyBreedCharacteristics());
    }
}

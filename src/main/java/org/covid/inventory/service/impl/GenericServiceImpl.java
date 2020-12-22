package org.covid.inventory.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

@Service
public class GenericServiceImpl {

	@Autowired
	MessageSource messageSource;
}

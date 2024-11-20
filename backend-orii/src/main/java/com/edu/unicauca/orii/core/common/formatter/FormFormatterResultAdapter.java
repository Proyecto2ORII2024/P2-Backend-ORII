package com.edu.unicauca.orii.core.common.formatter;

import org.springframework.stereotype.Service;

import com.edu.unicauca.orii.core.common.exception.BusinessRuleException;
import com.edu.unicauca.orii.core.common.exception.messages.MessageLoader;
import com.edu.unicauca.orii.core.common.exception.messages.MessagesConstant;

@Service
public class FormFormatterResultAdapter implements IFormFormatterResultOutputPort {

    @Override
    public void returnResponseErrorRequired(String message) {
        throw new BusinessRuleException(400, 
         MessageLoader.getInstance().getMessage(MessagesConstant.EM013, message));
    }
    
}
    


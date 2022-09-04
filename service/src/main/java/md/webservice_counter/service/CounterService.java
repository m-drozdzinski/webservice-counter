/*
 * The MIT License
 *
 * Copyright 2022 Marcin Drozdziński.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package md.webservice_counter.service;

import md.webservice_counter.model.db.CounterEntity;
import md.webservice_counter.model.db.CountersRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import md.webservice_counter.model.type.OperationType;
import md.webservice_counter.exception.NotImplementedException;
import md.webservice_counter.exception.ResourceAlreadyExistsException;
import md.webservice_counter.exception.ResourceNotFoundException;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CounterService {
    
    private static final Long DEFAULT_COUNTER_VALUE = 0L;
    private static final Long DEFAULT_INCREASE_STEP = 1L;
    
    private final CountersRepository countersRepository;
   
    
    public void createCounter(String name, Long value) {
        log.info("Creating new counter...");
        if(!countersRepository.findByName(name).isEmpty()) {
            log.info("Counter cannot be created, because it already exists");
            throw new ResourceAlreadyExistsException();
        }
        
        CounterEntity counterEntity = 
                new CounterEntity(
                        name, 
                        Optional.ofNullable(value)
                                .orElse(DEFAULT_COUNTER_VALUE)
                );
        countersRepository.save(counterEntity);
        log.info("New counter created");
    }
    
    public List<CounterEntity> getAllCounters() {
        log.info("Collecting all counter...");
        return countersRepository.findAll();
    }
    
    public CounterEntity getSingleCounter(String name) {
        log.info("Collecting single counter...");
        return countersRepository.findByName(name)
                .orElseThrow(() -> {
                    log.info("Counter does not exists");
                    return new ResourceNotFoundException();
                        });
    }


    public void operate(String name, OperationType operation) {
        log.info("Increasing counter's value...");
        CounterEntity counter = countersRepository.findByName(name)
                .orElseThrow(() -> {
                    log.info("Counter does not exists");
                    return new ResourceNotFoundException();
                        });
                
        switch(operation){
            case INCREMENT -> increaseCounterValue(counter);
            default -> throw new NotImplementedException();
        }
    }
    
    private void increaseCounterValue(CounterEntity counter) {
        counter.setCurrentValue(counter.getCurrentValue() + DEFAULT_INCREASE_STEP);
        countersRepository.save(counter);
        log.info("Counter's value has been increased");
    }
}

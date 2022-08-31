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
package md.demo.controller;

import md.demo.controller.type.OperationType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import static md.demo.controller.Endpoints.NAME_PATH_VARIABLE;
import static md.demo.controller.Endpoints.V1_COUNTER;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import md.demo.controller.dto.PostCountersRequestDto;
import md.demo.model.CounterService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import static md.demo.controller.Endpoints.V1_COUNTERS;
import md.demo.controller.dto.CounterDto;
import md.demo.controller.dto.GetCountersResponseDto;
import md.demo.controller.exception.CounterAlreadyExistsException;
import md.demo.controller.exception.CounterNotFoundException;
import md.demo.model.db.CounterEntity;
import md.demo.model.CounterMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@RestController
@RequiredArgsConstructor
public class CountersController {

    private final CounterService counterService;
    
    
    @GetMapping(value = V1_COUNTERS)
    public @ResponseBody GetCountersResponseDto getCounters() {
        return CounterMapper.map(counterService.getAllCounters());
    }
    
    
    @GetMapping(value = V1_COUNTER)
    public @ResponseBody CounterDto getCounter(
            @PathVariable(NAME_PATH_VARIABLE) String name
    ) {
        CounterEntity counter = counterService.getSingleCounter(name)
                .orElseThrow(() -> new CounterNotFoundException());
        return CounterMapper.map(counter);
    }
    
    
    @PostMapping(value = V1_COUNTERS)
    public void postCounters(
            @RequestBody @Validated PostCountersRequestDto request
    ) {
        if(!counterService.getSingleCounter(request.getName()).isEmpty()) {
            throw new CounterAlreadyExistsException();
        }
                
        counterService.createNewCounter(request.getName());
    }
    
    
    @PostMapping(value = V1_COUNTER)
    public void postCounters(
            @PathVariable(NAME_PATH_VARIABLE) String name,
            @RequestParam("operation") OperationType operation
    ) {
        CounterEntity counter = counterService.getSingleCounter(name)
                .orElseThrow(() -> new CounterNotFoundException());
        
        switch(operation){
            case INCREMENT -> counterService.increaseCounterValue(counter);
            default -> throw new RuntimeException();
        }
    }

}

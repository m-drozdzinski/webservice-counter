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
package md.webservicecounter.api.v1;

import java.util.List;
import md.webservicecounter.model.type.OperationType;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import md.webservicecounter.service.CounterService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import md.webservicecounter.model.dto.CounterDto;
import md.webservicecounter.model.CounterMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@RestController
@RequestMapping(ApiV1Endpoints.COUNTERS)
@RequiredArgsConstructor
public class CountersController {

    private final CounterService counterService;
    
    
    @GetMapping
    public @ResponseBody List<CounterDto> getCounters() {
        return CounterMapper.map(counterService.getAllCounters());
    }
    
    
    @GetMapping("/{name}")
    public @ResponseBody CounterDto getCounter(
            @PathVariable("name") String name
    ) {
        return CounterMapper.map(counterService.getSingleCounter(name));
    }
    
    
    @PostMapping
    public void postCounters(
            @RequestBody @Validated CounterDto request
    ) {                
        counterService.createCounter(request.getName(), request.getValue());
    }
    
    
    @PostMapping("/{name}")
    public void postCounters(
            @PathVariable("name") String name,
            @RequestParam("operation") OperationType operation
    ) {
        
        counterService.operate(name, operation);
    }

}

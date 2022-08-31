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
package md.demo.model;

import md.demo.model.db.CounterEntity;
import md.demo.model.db.CountersRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class CounterService {
    
    private static final Long DEFAULT_COUNTER_VALUE = 0L;
    
    private final CountersRepository countersRepository;
    
    public void createNewCounter(String name) {
        createNewCounter(name, DEFAULT_COUNTER_VALUE);
    }
    
    public void createNewCounter(String name, Long value) {
        CounterEntity counterEntity = new CounterEntity(name, value);
        countersRepository.save(counterEntity);
    }
    
    public List<CounterEntity> getAllCounters() {
        return countersRepository.findAll();
    }
    
    public Optional<CounterEntity> getSingleCounter(String name) {
        return countersRepository.findByName(name);
    }

    public void increaseCounterValue(CounterEntity counter) {
        counter.setCurrentValue(counter.getCurrentValue() + 1);
        countersRepository.save(counter);
    }
}

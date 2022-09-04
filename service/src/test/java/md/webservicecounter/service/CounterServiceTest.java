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
package md.webservicecounter.service;

import java.util.Optional;
import md.webservicecounter.model.type.OperationType;
import md.webservicecounter.exception.ResourceAlreadyExistsException;
import md.webservicecounter.exception.ResourceNotFoundException;
import md.webservicecounter.model.db.CounterEntity;
import md.webservicecounter.model.db.CountersRepository;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

public class CounterServiceTest {

    private final CountersRepository countersRepository = mock(CountersRepository.class);

    private CountersService sut;

    @BeforeEach
    void setup() {
        sut = new CountersService(countersRepository);
    }

    @Test
    public void createCounterWithValidValues_positive() {
        final String validName = "counter_1";
        final Long validValue = 0L;

        when(countersRepository.findByName(validName))
                .thenReturn(Optional.empty());

        sut.createCounter(validName, validValue);

        ArgumentCaptor<CounterEntity> captor = ArgumentCaptor.forClass(CounterEntity.class);
        verify(countersRepository).save(captor.capture());

        assertThat(captor.getValue().getName()).isEqualTo(validName);
        assertThat(captor.getValue().getCurrentValue()).isEqualTo(validValue);

    }

    @Test
    public void createCounterWithValidNameAndNoStartValue_positive() {
        final String validName = "counter_1";
        final Long noValue = null;
        final Long expectedValue = 0L;

        when(countersRepository.findByName(validName))
                .thenReturn(Optional.empty());

        sut.createCounter(validName, noValue);

        ArgumentCaptor<CounterEntity> captor = ArgumentCaptor.forClass(CounterEntity.class);
        verify(countersRepository).save(captor.capture());

        assertThat(captor.getValue().getName()).isEqualTo(validName);
        assertThat(captor.getValue().getCurrentValue()).isEqualTo(expectedValue);

    }

    @Test
    public void createCounterWithNameAlreadyUsed_negative() {
        final String nameWhichAlreadyExists = "counter_1";

        when(countersRepository.findByName(nameWhichAlreadyExists))
                .thenReturn(Optional.of(new CounterEntity(nameWhichAlreadyExists, 0L)));

        assertThrows(ResourceAlreadyExistsException.class,
                () -> sut.createCounter(nameWhichAlreadyExists, null));
    }

    @Test
    public void getSingleCounterWhichExists_positive() {
        final String name = "counter_1";
        final CounterEntity expectedCounter = new CounterEntity(name, 0L);

        when(countersRepository.findByName(name))
                .thenReturn(Optional.of(expectedCounter));

        CounterEntity counter = sut.getSingleCounter(name);

        verify(countersRepository).findByName(name);
        
        assertThat(counter).isEqualTo(expectedCounter);
    }

    @Test
    public void getSingleCounterWhichDoesNotExist_negative() {
        final String name = "counter_1";

        when(countersRepository.findByName(name))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> sut.getSingleCounter(name));
    }
    
    @Test
    public void increaseValueOfExistingCounter_postitive() {
        final String name = "counter_1";
        final Long initialValue = 0L;
        final Long expectedValue = 1L;
        final CounterEntity counter = new CounterEntity(name, initialValue);

        when(countersRepository.findByName(name))
                .thenReturn(Optional.of(counter));
        
        sut.operate(name, OperationType.INCREMENT);
        
        verify(countersRepository).findByName(name);
        
        ArgumentCaptor<CounterEntity> captor = ArgumentCaptor.forClass(CounterEntity.class);
        verify(countersRepository).save(captor.capture());

        assertThat(captor.getValue().getName()).isEqualTo(name);
        assertThat(captor.getValue().getCurrentValue()).isEqualTo(expectedValue);
    }
    
    @Test
    public void increaseValueOfNotExistingCounter_negative() {
        final String name = "not_existing_counter";

        when(countersRepository.findByName(name))
                .thenReturn(Optional.empty());
        
        assertThrows(ResourceNotFoundException.class,
                () -> sut.operate(name, OperationType.INCREMENT));
    }
}

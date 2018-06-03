package com.netcracker.komarov.news.service.impl;

import com.netcracker.komarov.TestConfig;
import com.netcracker.komarov.news.dao.entity.NewsStatus;
import com.netcracker.komarov.news.service.NewsService;
import com.netcracker.komarov.news.service.dto.entity.NewsDTO;
import com.netcracker.komarov.news.service.exception.LogicException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;

@WebAppConfiguration
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class NewsServiceImplTest {
    @Mock
    @Autowired
    private NewsService newsService;

    @Before
    public void setUp() {
        NewsDTO newsDTO1 = new NewsDTO(0L, null, "Gen t1", "b1", NewsStatus.GENERAL.toString());
        NewsDTO newsDTO2 = new NewsDTO(0L, null, "Cli t2", "b2", NewsStatus.CLIENT.toString());
        NewsDTO newsDTO3 = new NewsDTO(0L, null, "Cli t3", "b3", NewsStatus.CLIENT.toString());
        newsService.save(newsDTO1, 1);
        newsService.save(newsDTO2, 2);
        newsService.save(newsDTO3, 3);
        Collection<Long> clientIds = Stream.of(1L, 2L).collect(Collectors.toList());
        newsService.sendNewsToClient(clientIds, 2);
        newsService.sendNewsToClient(Collections.singleton(2L), 3);
    }

    @Test
    public void findAllNews() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyy HH:mm");
        String date = simpleDateFormat.format(new Date());
        Collection<NewsDTO> dtos = Stream.of(
                new NewsDTO(1L, date, "Gen t1", "b1", null),
                new NewsDTO(2L, date, "Cli t2", "b2", null),
                new NewsDTO(3L, date, "Cli t3", "b3", null))
                .collect(Collectors.toList());
        assertEquals(dtos, newsService.findAllNews());
    }

    @Test
    public void findAllNewsByClientId() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyy HH:mm");
        String date = simpleDateFormat.format(new Date());
        NewsDTO newsDTO2 = new NewsDTO(2L, date, "Cli t2", "b2", null);
        NewsDTO newsDTO3 = new NewsDTO(3L, date, "Cli t3", "b3", null);
        Collection<NewsDTO> dtos = Stream.of(newsDTO2, newsDTO3).collect(Collectors.toList());
        assertEquals(dtos, newsService.findAllNewsByClientId(2));
    }

    @Test
    public void findById() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyy HH:mm");
        String date = simpleDateFormat.format(new Date());
        NewsDTO newsDTO1 = new NewsDTO(1L, date, "Gen t1", "b1", null);
        assertEquals(newsDTO1, newsService.findById(1));
    }

    @Test
    public void save() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyy HH:mm");
        String date = simpleDateFormat.format(new Date());
        NewsDTO newsDTO1 = new NewsDTO(4L, date, "Gen t4", "b4", null);
        assertEquals(newsDTO1, newsService.
                save(new NewsDTO(0L, date, "Gen t4", "b4", NewsStatus.GENERAL.toString()), 1));

    }

    @Test
    public void findAllNewsByStatus() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyy HH:mm");
        String date = simpleDateFormat.format(new Date());
        NewsDTO newsDTO2 = new NewsDTO(2L, date, "Cli t2", "b2", null);
        NewsDTO newsDTO3 = new NewsDTO(3L, date, "Cli t3", "b3", null);
        Collection<NewsDTO> dtos = Stream.of(newsDTO2, newsDTO3).collect(Collectors.toList());
        assertEquals(dtos, newsService.findAllNewsByStatus(NewsStatus.CLIENT));
    }

    @Test
    public void sendNewsToClient() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyy HH:mm");
        String date = simpleDateFormat.format(new Date());
        NewsDTO newsDTO3 = new NewsDTO(3L, date, "Cli t3", "b3", null);
        assertEquals(newsDTO3, newsService.sendNewsToClient(Collections.singleton(1L), 3));

    }

    @Test
    public void update() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyy HH:mm");
        String date = simpleDateFormat.format(new Date());
        NewsDTO newsDTO2 = new NewsDTO(2L, date, "Client News", "News for all clients",
                NewsStatus.CLIENT.toString());
        NewsDTO res = new NewsDTO(2L, date, "Client News", "News for all clients",
                null);
        assertEquals(res, newsService.update(newsDTO2));
    }

    @Test(expected = LogicException.class)
    public void findGeneralNewsById() throws LogicException {
        assertNull(newsService.findGeneralNewsById(2));
    }

    @Test
    public void deleteById() {
        newsService = mock(NewsService.class);
        doNothing().when(newsService).deleteById(isA(Long.class));
        newsService.deleteById(10L);
        verify(newsService, times(1)).deleteById(10L);
    }

    @Test
    public void findAllNewsBySpecification() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyy HH:mm");
        String date = simpleDateFormat.format(new Date());
        Map<String, String> params = new HashMap<>();
        params.put("filter", "true");
        params.put("status", "client");
        Collection<NewsDTO> dtos = Stream.of(
                new NewsDTO(2L, date, "Cli t2", "b2", null),
                new NewsDTO(3L, date, "Cli t3", "b3", null))
                .collect(Collectors.toList());
        assertEquals(dtos, newsService.findAllNewsBySpecification(params));
    }
}
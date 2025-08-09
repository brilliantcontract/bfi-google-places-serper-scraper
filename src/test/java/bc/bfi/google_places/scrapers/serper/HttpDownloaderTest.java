package bc.bfi.google_places.scrapers.serper;

import java.io.IOException;
import org.junit.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import org.mockito.Mockito;

public class HttpDownloaderTest {

    @Test
    public void shouldRetryAndReturnResponseAfterNetworkIssues() throws Exception {
        HttpDownloader downloader = Mockito.spy(new HttpDownloader());
        Mockito.doThrow(new IOException())
                .doThrow(new IOException())
                .doReturn("ok")
                .when(downloader).executePost(Mockito.anyString(), Mockito.anyString());
        Mockito.doNothing().when(downloader).pause(Mockito.anyLong());
        Mockito.doNothing().when(downloader).showError(Mockito.anyString());

        String result = downloader.sendPostRequest("url", "{}");

        assertThat(result, is("ok"));
        Mockito.verify(downloader, Mockito.times(3)).executePost(Mockito.anyString(), Mockito.anyString());
        Mockito.verify(downloader, Mockito.times(2)).pause(Mockito.anyLong());
        Mockito.verify(downloader, Mockito.never()).showError(Mockito.anyString());
    }

    @Test
    public void shouldShowErrorAfterThreeFailedRetries() throws Exception {
        HttpDownloader downloader = Mockito.spy(new HttpDownloader());
        Mockito.doThrow(new IOException())
                .doThrow(new IOException())
                .doThrow(new IOException())
                .doThrow(new IOException())
                .when(downloader).executePost(Mockito.anyString(), Mockito.anyString());
        Mockito.doNothing().when(downloader).pause(Mockito.anyLong());
        Mockito.doNothing().when(downloader).showError(Mockito.anyString());

        String result = downloader.sendPostRequest("url", "{}");

        assertThat(result, nullValue());
        Mockito.verify(downloader, Mockito.times(4)).executePost(Mockito.anyString(), Mockito.anyString());
        Mockito.verify(downloader).pause(5000L);
        Mockito.verify(downloader).pause(15000L);
        Mockito.verify(downloader).pause(60000L);
        Mockito.verify(downloader).showError(Mockito.anyString());
    }
}

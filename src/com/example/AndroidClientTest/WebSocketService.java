package com.example.AndroidClientTest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import javax.websocket.ClientEndpointConfig;
import javax.websocket.DeploymentException;
import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.MessageHandler;
import javax.websocket.Session;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EService;
import org.androidannotations.annotations.SystemService;
import org.androidannotations.annotations.UiThread;
import org.glassfish.tyrus.client.ClientManager;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

@EService
public class WebSocketService extends IntentService {

	@SystemService
	NotificationManager notificationManager;
	
	private static CountDownLatch messageLatch;

	public WebSocketService() {
		super(WebSocketService.class.getSimpleName());
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		/*showToast();

		Log.i("TYRUS-TEST", "### 0 Button.onClick");

		try {
			ClientManager client = ClientManager.createClient();

			Log.i("TYRUS-TEST", "### 1 AsyncTask.doInBackground");
			client.connectToServer(new Endpoint() {
				@Override
				public void onOpen(Session session,
						EndpointConfig EndpointConfig) {

					try {
						session.addMessageHandler(new MessageHandler.Whole<String>() {
							@Override
							public void onMessage(String message) {
								Log.i("TYRUS-TEST",
										"### 3 Tyrus Client onMessage: "
												+ message);
							}
						});

						Log.i("TYRUS-TEST", "### 2 Tyrus Client onOpen");
						session.getBasicRemote().sendText(
								"Do or do not, there is no try.");
					} catch (IOException e) {
						// do nothing
					}
				}
			}, ClientEndpointConfig.Builder.create().build(), URI
					.create("ws://192.168.1.17:8025/websocket/teste"));

		} catch (DeploymentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				System.in));
		try {
			reader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			Thread.sleep(100000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		messageLatch = new CountDownLatch(100);

        final ClientEndpointConfig cec = ClientEndpointConfig.Builder.create().build();

        ClientManager client = ClientManager.createClient();
        try {
			client.connectToServer(new Endpoint() {

			    @Override
			    public void onOpen(Session session, EndpointConfig config) {
			        try {
			            session.addMessageHandler(new MessageHandler.Whole<String>() {

			                @Override
			                public void onMessage(String message) {
			                    Log.i("Mensagem Recebida",message);
			                    messageLatch.countDown();
			                }
			            });
			            session.getBasicRemote().sendText("Enviando mensagem do Android");
			            /*while(true){
			            	session.getBasicRemote().sendText(SENT_MESSAGE);
			            	try {
								Thread.sleep(3000);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
			            }*/
			        } catch (IOException e) {
			            e.printStackTrace();
			        }
			    }
			}, cec, new URI("ws://192.168.1.17:8025/websocket/teste"));
			messageLatch.await(100, TimeUnit.SECONDS);
		} catch (DeploymentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        try {
			Thread.sleep(100000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
	}

	@UiThread
	void showToast() {
		Toast.makeText(getApplicationContext(), "Hello World!",
				Toast.LENGTH_LONG).show();
	}
}


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.aldebaran.qi.CallError;
import com.aldebaran.qi.helper.proxies.ALBehaviorManager;
import com.aldebaran.qi.helper.proxies.ALMotion;
import com.aldebaran.qi.helper.proxies.ALTextToSpeech;
import com.argetgames.arget2d.game.Gameloop;
import com.argetgames.arget2d.input.Mouse;
import com.argetgames.arget2d.menu.Button;


public class MemoryGame extends Gameloop{
		private ALMotion m;
		private ALBehaviorManager b;
		private ALTextToSpeech tts;
		final int size = 4;
		private Button[] btn = new Button[size*size];
		int board_colors[] = new int[size * size];
		int showing_piece[] = new int[size * size]; //0 : back, 1:front, 2:removed 
		int colors[] = {
				0xFFFF0000,//RÖD
				0xFF00FF00,//GRÖN
				0xFF0000FF,//BLÅ
				0xFFFFFF00,//GUL
				0xFFFF00FF,//LILA
				0xFF00FFFF,//CYAN
				0xFFFFFFFF,//VIT
				0xFFf58d42,//ORANGE
		};
		
		final int display_matching_tiles_timer = Gameloop.global_ups * 1;
		int display_matching_tiles = 0;
		

		final int display_hints_timer = (int)(Gameloop.global_ups * 1.5f);
		int display_hints = 1;
		boolean show_hints=false;
		
		int piece_1 = -1, piece_2 = -1;
		
		int message = 0;

		public MemoryGame(int width, int height, int scale) {
			super(width, height, scale);
			debug_log=false;
			loadContent();
		}
		
		private void loadContent(){
		    try{
		        // Start the application and create a session.
		        Main.application.start();
		        // A session has been created. It can be retrieved this way:
		        m = new ALMotion(Main.application.session());
		        b = new ALBehaviorManager(Main.application.session());
		        //tts = new ALTextToSpeech(Main.application.session());
		        
		        
		    }
		    catch(Exception e){
		        // The application could not be started.
		        e.printStackTrace();
		    }
		    int padding_x = 10;
			int padding_y = 10;
			int board_width = WIDTH - (size + 1) * padding_x;
			int piece_width = board_width / size;
			
			int board_height = HEIGHT - (size + 1) * padding_y;
			int piece_height = board_height / size;
			for(int y = 0; y < size; y++)
			{
				for(int x = 0; x < size; x++)
				{
					//renderer.fillRect(padding_x + x * (piece_width + padding_x), padding_y + y * (piece_height + padding_y), piece_width, piece_height, 0xff555555);
					btn[x + y*size] = new Button(padding_x + x * (piece_width + padding_x), padding_y + y * (piece_height + padding_y), piece_width, piece_height);
				}
			}
		    

			ArrayList<Integer> col = new ArrayList<Integer>();
			for(int i = 0; i < colors.length*2; i++)
			{
				col.add(colors[i%colors.length]);
			}
			Random r = new Random();
			for(int i = 0; i < showing_piece.length; i++)
			{
				
				showing_piece[i] = 0;
				int index = r.nextInt(col.size());
				int c = col.get(index);
				board_colors[i] = c;//colors[i/2];
				col.remove(index);
			}
		    
		}

		int x = 0;
		int y = 0;
		
		public void thread_func(int index){
			final int temp2=index;
			Thread t = new Thread(
					new Runnable(){
						public void run()
						{
							try {
								
								
								switch(get_movement(temp2))
								{
								case 1: upl();break;
								case 2: upr();break;
								case 3: dwl();break;
								case 4: dwr();break;
								}
							} catch (Exception e) {
							// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
				);
				t.start();
			
		}
		
		
		public int get_movement(int index) {
			int xpos=index%size;
			int ypos=index/size;
			if((ypos<size/2)){
				if(xpos<size/2)
				{
					return 1;//upl()
				}
				else{
					return 2;//upr()
				}
			}
			else{
				if(xpos<size/2)
				{
					return 3;//dwl()
				}
				else{
					return 4;//dwr()
				}
			}
			
		}
		
		public void dwl() throws CallError, InterruptedException{
			m.changeAngles("HeadYaw", -0.5f, 0.1f);
			m.changeAngles("RShoulderRoll", -1.0f, 1.0f);
			m.changeAngles("HipRoll", -0.5f, 0.5f);
			
			b.stopAllBehaviors();
			b.startBehavior("Speak_down_left");
			m.angleInterpolationWithSpeed("HeadYaw", 0.0f, 0.05f);
		}
		public void dwr() throws CallError, InterruptedException{
			m.changeAngles("HeadYaw", 0.5f, 0.1f);
			m.changeAngles("LShoulderRoll", 1.0f, 1.0f);
			m.changeAngles("HipRoll", 0.5f, 0.5f);
			
			b.stopAllBehaviors();
			b.startBehavior("Speak_down_right");
			m.angleInterpolationWithSpeed("HeadYaw", 0.0f, 0.05f);
		}
		public void upl() throws CallError, InterruptedException{
			m.changeAngles("HeadYaw", -0.5f, 0.1f);
			m.changeAngles("RShoulderRoll", -1.5f, 1.0f);
			m.changeAngles("RShoulderPitch", -1.5f, 1.0f);
			m.changeAngles("HipRoll", 0.3f, 0.5f);
			m.changeAngles("RElbowRoll", 0.5f, 1.0f);
			b.stopAllBehaviors();
			b.startBehavior("Speak_up_left");
			m.angleInterpolationWithSpeed("HeadYaw", 0.0f, 0.05f);
		}
		public void upr() throws CallError, InterruptedException{
			m.changeAngles("HeadYaw", 0.5f, 0.1f);
			m.changeAngles("LShoulderRoll", 1.5f, 1.0f);
			m.changeAngles("LShoulderPitch", -1.5f, 1.0f);
			m.changeAngles("HipRoll", -0.3f, 0.5f);
			m.changeAngles("LElbowRoll", -0.5f, 1.0f);
			
			b.stopAllBehaviors();
			b.startBehavior("Speak_up_right");
			m.angleInterpolationWithSpeed("HeadYaw", 0.0f, 0.05f);
		}
		int globalcount=0;
		int temp_index=0;
		public void updateGame() {
			x++;
			y += x-WIDTH/2;
			x%= WIDTH;
			y%= HEIGHT;
			if(show_hints&&display_hints-- <= 0 )
			{
				display_hints = display_hints_timer;
				thread_func(temp_index);
				show_hints=false;
			}
			
			if(display_matching_tiles-- >= 0 )
			{
				if(display_matching_tiles == 0)
				{
					if(piece_2 != -1 && piece_1 != -1 && board_colors[piece_2] == board_colors[piece_1])
					{
						showing_piece[piece_2] = 2;
						showing_piece[piece_1] = 2;
					}else {
					
						if(piece_2 != -1)
						{
							showing_piece[piece_2] = 0;
						}
						if(piece_1 != -1)
						{
							showing_piece[piece_1] = 0;
						}
					}
					piece_1 = -1;
					piece_2 = -1;
				}
			}else{
				for(int y = 0; y < size; y++)
				{
					for(int x = 0; x < size; x++)
					{
						int index = x + y *size;
						if(showing_piece[index] == 2)continue;
						
						btn[index].update(Mouse.getMouse().getMouseX(), Mouse.getMouse().getMouseY());
						if(btn[index].getClicked())
						{
							System.out.println("Pressed: "+ x + ", " + y);
							if(piece_2 != index && piece_1 != index)
							{
								
								piece_2 = piece_1;
								piece_1 = index;
								showing_piece[index] = 1;
								
								if(piece_2 == -1)
								{
									
									for(int i=0;i<board_colors.length;i++){
										if((i!=index)&&(board_colors[i]==board_colors[index]))
										{
											temp_index=i;
											break;
										}
										
									}
									
									display_hints = display_hints_timer;
									show_hints=true;
									//
								}
								if(piece_2 != -1)
								{
									show_hints=false;
									display_matching_tiles = display_matching_tiles_timer;
								}
							}	
						}
					
					}
				}
			}
			/*
			if(x == WIDTH/2)
			{
				Thread t = new Thread(new Runnable(){
					public void run()
					{
						try {
							m.openHand("LHand");
							m.closeHand("LHand");
					        m.openHand("RHand");
					        m.closeHand("RHand");
					       
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
				t.start();
			}*/
		}

		public void draw() {
			renderer.useAlpha(false);
			for(int y = 0; y < size; y++)
			{
				for(int x = 0; x < size; x++)
				{
					int index = x + y * size;
					int color = 0xff444444;
					if(showing_piece[index] == 1)
						color = board_colors[index];
					else if(showing_piece[index] == 2)
						continue;
					btn[index].draw(renderer, color);
				}
			}
		}
}

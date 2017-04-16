//
//package server.rsa;
//
//import io.netty.buffer.ByteBuf;
//import io.netty.buffer.Unpooled;
//import io.netty.channel.Channel;
//import io.netty.channel.ChannelFuture;
//import io.netty.channel.ChannelFutureListener;
//import io.netty.channel.ChannelHandlerContext;
//import io.netty.handler.codec.ByteToMessageDecoder;
//import io.netty.util.AttributeKey;
//
//import java.math.BigInteger;
//import java.security.SecureRandom;
//import java.util.List;
//
//import Connection;
//import Constants;
//import Server;
//import server.game.player.Client;
//import server.game.player.Player;
//import server.game.player.PlayerHandler;
//import server.game.player.PlayerSave;
//import Packet;
//import PacketBuilder;
//import server.login.RS2ProtocolDecoder;
//import Misc;
//
//public class RS2LoginProtocolDecoder extends ByteToMessageDecoder
//{
//
//	private static final BigInteger RSA_MODULUS = new BigInteger( "102200103636873648286099675067974502450963162824789462939669453204637534708832227397181128258393771103744972685345424289538958089969741477606431995076197844427696877493958235202388475308349765625372372259423102898146881431486875600758679527975254229662345801405480136890592902637282963233289416324923535617551" );
//	private static final BigInteger RSA_EXPONENT = new BigInteger( "76112465604292674756976256781935387882060668800102600167224417840486271786451673936884759876644997177649032253938072672289202533763265667317923200873995810050131158050625394558067898409285580696489738868307003446954066623867687180418266973040670881316802062210916962937719942389452765026790348461907083411009" );
//
//	public static final AttributeKey<Client> KEY = AttributeKey.valueOf( "client.KEY" );
//
//	private enum State {
//		HANDSHAKE, LOGIN
//	}
//
//	private State state = State.HANDSHAKE;
//
//
//	private Client load( final Channel session, final int uid, String name, String pass, final ISAACRandomGen inC, ISAACRandomGen outC, int clientVersion)
//	{
//		int loginDelay = 1;
//		int returnCode = 2;
//		name = name.trim();
//		name = name.toLowerCase();
//		pass = pass.toLowerCase();
//		String censoredNames[] = {
//			"ass", "fuck", "cock", "fuckk", "  ", "faggot", "fagot", "fuckkk",
//			"mawd", "cunt", "bitch", "dick", "pussy", "vagina", "nigger",
//			"negro", "spick", "gay", "lesbian", "d1ck"
//		};
//		for( String s: censoredNames ) {
//			if( name.contains( s ) && ! name.equalsIgnoreCase( "Mod True" ) ) {
//				returnCode = 4;
//			}
//		}
//		if( ! name.matches( "[A-Za-z0-9 ]+" ) ) {
//			returnCode = 4;
//		}
//		if( name.length() > 12 ) {
//			returnCode = 8;
//		}
//		Client cl = new Client( session, - 1 );
//		cl.playerName = Misc.optimizeText( name );
//		cl.playerName2 = cl.playerName;
//		cl.playerPass = pass;
//		cl.setInStreamDecryption( inC );
//		cl.setOutStreamDecryption( outC );
//		cl.outStream.packetEncryption = outC;
//		cl.saveCharacter = false;
//		char first = name.charAt( 0 );
//		cl.properName = Character.toUpperCase( first ) + name.substring( 1, name.length() );
//		if( Connection.isNamedBanned( cl.playerName ) ) {
//			returnCode = 4;
//		}
//		if( PlayerHandler.isPlayerOn( name ) ) {
//			returnCode = 5;
//		}
//		if( PlayerHandler.playerCount >= Constants.MAX_PLAYERS ) {
//			returnCode = 7;
//		}
//		if( Server.UpdateServer ) {
//			returnCode = 14;
//		}
//		if( clientVersion != Constants.CLIENT_VERSION ) {
//			returnCode = 18;
//		}
//		if( returnCode == 2 ) {
//			int load = PlayerSave.loadGame( cl, cl.playerName, cl.playerPass );
//			/*
//			 * if (load == 0) { cl.addStarter = true; }
//			 */
//			if( load == 3 ) {
//				returnCode = 3;
//				cl.saveFile = false;
//			} else {
//				for( int i = 0; i < cl.playerEquipment.length; i ++ ) {
//					if( cl.playerEquipment[ i ] == 0 ) {
//						cl.playerEquipment[ i ] = - 1;
//						cl.playerEquipmentN[ i ] = 0;
//					}
//				}
//				if( ! Server.playerHandler.newPlayerClient( cl ) ) {
//					returnCode = 7;
//					cl.saveFile = false;
//				} else {
//					cl.saveFile = true;
//				}
//			}
//		}
//
//		cl.packetType = - 1;
//		cl.packetSize = 0;
//		PacketBuilder bldr = new PacketBuilder();
//		bldr.put( ( byte )returnCode );
//		if( returnCode == 2 ) {
//			cl.saveCharacter = true;
//			if( cl.playerRights == 3 ) {
//				bldr.put( ( byte )2 );
//			} else {
//				bldr.put( ( byte )cl.playerRights );
//			}
//		} else if( returnCode == 21 ) {
//			bldr.put( ( byte )loginDelay );
//		} else {
//			bldr.put( ( byte )0 );
//		}
//		cl.isActive = true;
//		bldr.put( ( byte )0 );
//		Packet pkt = bldr.toPacket();
//		session.writeAndFlush( pkt );
//		return cl;
//	}
//
//
//	private synchronized String readRS2String( ByteBuf in )
//	{
//		StringBuilder sb = new StringBuilder();
//		for( char character = '\0'; character != '\n' && in.isReadable(); character = ( char )in.readByte() ) {
//			sb.append( character );
//		}
//		return sb.toString();
//	}
//
//
//	@Override
//	protected void decode( ChannelHandlerContext ctx, ByteBuf buf, List<Object> out ) throws Exception
//	{
//		switch( state ) {
//			case HANDSHAKE:
//				if( buf.readableBytes() < 2 ) {
//					return;
//				}
//				int protocol = buf.readUnsignedByte();
//				int nameHash = buf.readUnsignedByte();
//				if( protocol == 14 ) {
//					long serverSessionKey = new SecureRandom().nextLong();
//					PacketBuilder bldr = new PacketBuilder();
//					bldr.putLong( 0 );
//					bldr.put( ( byte )0 );
//					bldr.putLong( serverSessionKey );
//					ctx.channel().writeAndFlush( bldr.toPacket() );
//					state = State.LOGIN;
//				}
//				System.out.println(state);
//				break;
//			case LOGIN:
//				int loginPacketSize = - 1,
//				loginEncryptPacketSize = - 1;
//				if( 2 <= buf.readableBytes() ) {
//					int loginType = buf.readUnsignedByte(); // should be 16 or 18
//					if( loginType != 16 && loginType != 18 ) {
//						System.out.println( "Illegal login type: " + loginType );
//						return;
//					}
//					loginPacketSize = buf.readUnsignedByte();
//					loginEncryptPacketSize = loginPacketSize - ( 36 + 1 + 1 + 2 );
//					if( loginPacketSize <= 0 || loginEncryptPacketSize <= 0 ) {
//						System.out.println( "Zero or negative login size." );
//						return;
//					}
//				} else {
//					System.out.println( buf.readableBytes() + " - 1" );
//					return;
//				}
//				if( loginPacketSize <= buf.readableBytes() ) {
//					int magic = buf.readUnsignedByte();
//					int version = buf.readUnsignedShort();
//					if( magic != 255 ) {
//						System.out.println( "Wrong magic id." + magic );
//						return;
//					}
//					if( version != 317 ) {
//						System.out.println( "Wrong client version" + version );
//						return;
//					}
//					@SuppressWarnings( "unused" )
//					int lowMem = buf.readUnsignedByte();
//					for( int i = 0; i < 9; i ++ ) {
//						buf.readInt();
//					}
//					loginEncryptPacketSize -- ;
//					if( loginEncryptPacketSize != ( buf.readUnsignedByte() ) ) {
//						System.out.println( "Encrypted size mismatch." );
//						return;
//					}
//					byte[] encryptionBytes = new byte[ loginEncryptPacketSize ];
//					buf.readBytes( encryptionBytes );
//					ByteBuf rsaBuffer = Unpooled.wrappedBuffer( new BigInteger( encryptionBytes ).modPow( RSA_EXPONENT, RSA_MODULUS ).toByteArray() );
//					if( ( rsaBuffer.readUnsignedByte() ) != 10 ) {
//						System.out.println( "Encrypted id != 10." );
//						return;
//					}
//					long clientSessionKey = rsaBuffer.readLong();
//					long serverSessionKey = rsaBuffer.readLong();
//					int uid = rsaBuffer.readInt();
//
//					if( uid == 0 || uid == 99735086 ) {
//						System.out.println( "UID " + uid );
//						return;
//					}
//					if( uid != 889057271 ) {
//						System.out.println( "UID " + uid );
//						return;
//					}
//					int clientVersion = rsaBuffer.readInt();
//					String name = readRS2String( rsaBuffer );
//					String pass = readRS2String( rsaBuffer );
//					int sessionKey[] = new int[ 4 ];
//					sessionKey[ 0 ] = ( int )( clientSessionKey >> 32 );
//					sessionKey[ 1 ] = ( int )clientSessionKey;
//					sessionKey[ 2 ] = ( int )( serverSessionKey >> 32 );
//					sessionKey[ 3 ] = ( int )serverSessionKey;
//					ISAACRandomGen inC = new ISAACRandomGen( sessionKey );
//					for( int i = 0; i < 4; i ++ ) {
//						sessionKey[ i ] += 50;
//					}
//					ISAACRandomGen outC = new ISAACRandomGen( sessionKey );
//					out.add( load( ctx.channel(), uid, name, pass, inC, outC, clientVersion ) );
//					ctx.channel().pipeline().remove( "decoder" );
//					ctx.channel().pipeline().addLast( "decoder", new RS2ProtocolDecoder( inC ) );
//					break;
//				}
//		}
//	}
//
// }

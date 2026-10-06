package com.erickandrea.rpg

import android.app.Activity
import android.os.Bundle
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.Gravity
import android.widget.*
import kotlin.math.max
import kotlin.random.Random

class MainActivity : Activity() {
 private lateinit var scene:TextView; private lateinit var stats:TextView; private lateinit var buttons:LinearLayout
 private var hpE=118; private var hpA=105; private var gold=50; private var pot=2; private var bond=50
 private var enemyHp=0; private var enemyName=""; private var level=1; private var xp=0; private var next=100
 private fun box(c:Int)=GradientDrawable().apply{setColor(c);cornerRadius=28f}
 override fun onCreate(b:Bundle?){super.onCreate(b); build(); intro()}
 private fun build(){
  val root=LinearLayout(this).apply{orientation=LinearLayout.VERTICAL;setPadding(24,18,24,18);setBackgroundColor(Color.rgb(15,20,32))}
  root.addView(TextView(this).apply{text="ERICK & ANDREA  •  EL VÍNCULO AZUL";textSize=24f;setTextColor(Color.WHITE);gravity=Gravity.CENTER;setPadding(0,0,0,12)})
  stats=TextView(this).apply{textSize=14f;setTextColor(Color.WHITE);setPadding(18,12,18,12);background=box(Color.rgb(28,37,55))};root.addView(stats,LinearLayout.LayoutParams(-1,72))
  scene=TextView(this).apply{textSize=18f;setTextColor(Color.WHITE);setPadding(20,20,20,20);background=box(Color.rgb(23,31,47))};root.addView(scene,LinearLayout.LayoutParams(-1,0,1f))
  buttons=LinearLayout(this).apply{orientation=LinearLayout.VERTICAL;setPadding(0,10,0,0)};root.addView(buttons);setContentView(root);update()
 }
 private fun update(){stats.text="ERICK Lv.$level  ❤️ $hpE   |   ANDREA Lv.$level  💙 $hpA   |   💙 VÍNCULO $bond/100   |   🪙 $gold   🧪 $pot   |   XP $xp/$next"}
 private fun screen(t:String,b:String,opts:List<Pair<String,()->Unit>>){scene.text="$t\n\n$b";buttons.removeAllViews();opts.forEach{(txt,fn)->buttons.addView(Button(this).apply{text=txt;textSize=15f;setOnClickListener{fn()}},LinearLayout.LayoutParams(-1,56).apply{setMargins(0,4,0,4)})};update()}
 private fun intro()=screen("EL COMIENZO","Una tarde normal cambia para siempre cuando Erick y Andrea encuentran un misterioso cristal azul. Al tocarlo juntos, un portal aparece.",listOf("✨ Comenzar aventura" to {portal()}))
 private fun portal(){bond=minOf(100,bond+5);screen("AETHERIA","Un mundo fantástico se extiende frente a ustedes. Una voz dice: «Su vínculo es la última esperanza de este mundo». La aventura comienza.",listOf("🌲 Bosque de los Recuerdos" to {forest()},"🏰 Ciudad del Eco" to {city()},"🗺️ Explorar el valle" to {valley()}))}
 private fun valley(){screen("VALLE DEL ENCUENTRO","Encuentran una poción y las huellas de una criatura.",listOf("🧪 Tomar la poción" to {pot++;bond=minOf(100,bond+2);portal()},"⚔️ Seguir las huellas" to {battle("Lobo del Valle",55,10,45){portal()}}))}
 private fun forest(){screen("BOSQUE DE LOS RECUERDOS","Los árboles muestran recuerdos de ustedes. Una sombra intenta convertirlos en dudas.",listOf("💙 Erick toma la mano de Andrea" to {bond=minOf(100,bond+8);gain(30);portal()},"⚔️ Enfrentar la sombra" to {battle("Sombra de la Duda",85,13,90){bond=minOf(100,bond+10);portal()}}))}
 private fun city(){screen("CIUDAD DEL ECO","La gente ha perdido recuerdos importantes. Bajo la ciudad duerme una criatura que se alimenta de ellos.",listOf("🤝 Ayudar a una niña" to {gold+=30;bond=minOf(100,bond+5);ruins()},"⚔️ Ir a las ruinas" to {battle("Guardián del Eco",105,16,120){ruins()}}))}
 private fun ruins(){screen("RUINAS","Una criatura devora recuerdos desde una cámara antigua.",listOf("⚔️ Enfrentar al Devorador" to {battle("Devorador de Recuerdos",150,18,180){bond=minOf(100,bond+12);portal()}},"↩️ Regresar" to {portal()}))}
 private fun battle(name:String,hp:Int,atk:Int,reward:Int,win:()->Unit){enemyName=name;enemyHp=hp;combat(atk,reward,win)}
 private fun combat(atk:Int,reward:Int,win:()->Unit){screen("⚔️ $enemyName","HP enemigo: $enemyHp\n\nErick y Andrea luchan juntos.",listOf("⚔️ Golpe de Erick" to {hit(max(1,12+level*4+Random.nextInt(7)),atk,reward,win)},"🔮 Magia de Andrea" to {hit(max(2,14+level*4+Random.nextInt(7)),atk,reward,win)},"💙 ATAQUE COMBINADO" to {if(bond>=60)hit(35+level*7,atk,reward,win)},"🧪 Poción ($pot)" to {if(pot>0){pot--;hpE=minOf(118+level*18,hpE+35);hpA=minOf(105+level*15,hpA+30);enemyTurn(atk,reward,win)}}))}
 private fun hit(dmg:Int,atk:Int,reward:Int,win:()->Unit){enemyHp=max(0,enemyHp-dmg);if(enemyHp<=0){gain(reward);gold+=reward/2;win();return};enemyTurn(atk,reward,win)}
 private fun enemyTurn(atk:Int,reward:Int,win:()->Unit){val d=max(1,atk-6);if(Random.nextBoolean())hpE=max(0,hpE-d)else hpA=max(0,hpA-d);if(hpE<=0&&hpA<=0){hpE=118;hpA=105;screen("💫 EL VÍNCULO LOS SALVA","Han caído, pero el cristal azul los devuelve al último refugio.",listOf("Continuar" to {portal()}))}else combat(atk,reward,win)}
 private fun gain(n:Int){xp+=n;while(xp>=next){xp-=next;next=(next*1.35).toInt();level++;hpE=118+level*18;hpA=105+level*15;bond=minOf(100,bond+3)}}
}

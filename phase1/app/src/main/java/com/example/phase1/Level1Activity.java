package com.example.phase1;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import java.lang.reflect.Array;
import com.example.phase1.BackendStorage.GameManager;

import pl.droidsonroids.gif.GifImageView;

public class Level1Activity extends GameManager {
  ImageView backgroundOne;
  ImageView backgroundTwo;
  ImageView backgroundThree;
  ImageView backgroundFour;
  ValueAnimator animator;
  GifImageView hero;
  GifImageView coin0;
  GifImageView coin1;
  GifImageView coin2;
  GifImageView enemy;
  Level1Manager manager;
  int activityLevel; // Day or Night background
  int[] heroAction = new int[4]; // animations for hero, stand, walk, hurt and attack
  int[] enemyAction = new int[4]; // animations for enemy, stand, walk, hurt and attack

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Intent intent = getIntent();
    setCurrPlayer(intent.getIntExtra(sendString, 0));
    // Set our window to fullscreen without the bar at the top.
    this.getWindow()
        .setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

    // Remove the title.
    this.requestWindowFeature(Window.FEATURE_NO_TITLE);
    setActivityLevel();
    setContentView(activityLevel);

    backgroundOne = findViewById(R.id.grass);
    backgroundTwo = findViewById(R.id.grass1);
    backgroundThree = findViewById(R.id.vegetation);
    backgroundFour = findViewById(R.id.vegetation2);
    animator = ValueAnimator.ofFloat(0.0f, 1.0f);
    hero = findViewById(R.id.hero);
    coin0 = findViewById(R.id.c1);
    coin1 = findViewById(R.id.c2);
    coin2 = findViewById(R.id.c3);
    enemy = findViewById(R.id.enemy);
    manager = new Level1Manager();

    setHeroAction();
    setEnemyAction();
    heroStandAnimation();

    animator.setRepeatCount(1);
    animator.setInterpolator(new LinearInterpolator());
    animator.setDuration(17000L);

    enemy.setX(manager.Objects.get(0).getX());
    coin0.setX(manager.Objects.get(1).getX());
    coin1.setX(manager.Objects.get(2).getX());
    coin2.setX(manager.Objects.get(3).getX());

    // Move Graphic Components Right if Left Button is Pressed
    setLeftButton();

    // Move Graphic Components Left if Right Button is Pressed
    setRightButton();

    setAttackButton();
  }

  private void setActivityLevel() {
    int DayOrNight = getDayOrNight();
    if (DayOrNight == 0) activityLevel = R.layout.n_activity_level1;
    else if (DayOrNight == 1) activityLevel = R.layout.activity_level1;
  }

  private void setHeroAction() {
    int heroType = getCharacter();
    if (heroType == 0) {
      Array.set(heroAction, 0, R.drawable.hero1); // stand/unmoved
      Array.set(heroAction, 1, R.drawable.walk1); // walk animation
      Array.set(heroAction, 2, R.drawable.hurt1); // get hurt animation
      Array.set(heroAction, 3, R.drawable.attack1); // attack animation
    } else if (heroType == 1) {
      Array.set(heroAction, 0, R.drawable.knight); // stand/unmoved
      Array.set(heroAction, 1, R.drawable.walk); // walk animation
      Array.set(heroAction, 2, R.drawable.hurt2); // get hurt animation
      Array.set(heroAction, 3, R.drawable.attack); // attack animation
    }
  }

  private void setEnemyAction() {
    int enemyType = 0; // default, since we only have one type of enemy right now
    if (enemyType == 0) {
      Array.set(enemyAction, 0, R.drawable.mage); // stand/unmoved
      Array.set(enemyAction, 1, R.drawable.walke); // walk animation
      Array.set(enemyAction, 2, R.drawable.walke); // get hurt animation
      Array.set(enemyAction, 3, R.drawable.e_attack); // attack animation
    }
  }

  private void heroStandAnimation() {
    hero.setImageResource(heroAction[0]);
  }

  private void heroWalkAnimation() {
    hero.setImageResource(heroAction[1]);
  }

  private void heroHurtAnimation() {
    hero.setImageResource(heroAction[2]);
  }

  private void heroAttackAnimation() {
    hero.setImageResource(heroAction[3]);
  }

  private void heroFacingLeft() {
    hero.setScaleX(-1f);
  }

  private void heroFacingRight() {
    hero.setScaleX(1f);
  }

  private void enemyStandAnimation() {
    enemy.setImageResource(enemyAction[0]);
  }

  private void enemyWalkAnimation() {
    enemy.setImageResource(enemyAction[1]);
  }

  private void enemyHurtAnimation() {
    enemy.setImageResource(enemyAction[2]);
  }

  private void enemyAttackAnimation() {
    enemy.setImageResource(enemyAction[3]);
  }

  private void enemyFacingLeft() {
    enemy.setScaleX(-1f);
  }

  private void enemyFacingRight() {
    enemy.setScaleX(1f);
  }

  private void imageInvisable(GifImageView image) {
    image.setVisibility(View.INVISIBLE);
  }

  private void setLeftButton() {
    Button left = findViewById(R.id.left);
    left.setOnTouchListener(
        new View.OnTouchListener() {
          public boolean onTouch(View v, MotionEvent event) {
            int action = event.getActionMasked();
            switch (action) {
              case MotionEvent.ACTION_DOWN:
                hero.setX(manager.heroMoveLeft());
                heroFacingLeft();
                heroWalkAnimation();

                if (((Monster) manager.Objects.get(0)).isMoveLeft()) {
                  enemyFacingLeft();
                } else {
                  enemyFacingRight();
                }

                if (((Monster) manager.Objects.get(0)).isAttack() && enemy.isShown()) {
                  enemyAttackAnimation();
                  heroHurtAnimation();

                } else {
                  enemyWalkAnimation();
                  heroWalkAnimation();
                }

                break;
              case MotionEvent.ACTION_UP:
                manager.update();
                enemy.setX(manager.Objects.get(0).getX());
                coin0.setX(manager.Objects.get(1).getX());
                coin1.setX(manager.Objects.get(2).getX());
                coin2.setX(manager.Objects.get(3).getX());
                if (!manager.Objects.get(0).getStates()) {
                  imageInvisable(enemy);
                }
                if (!manager.Objects.get(1).getStates()) {
                  imageInvisable(coin0);
                }
                if (!manager.Objects.get(2).getStates()) {
                  imageInvisable(coin1);
                }
                if (!manager.Objects.get(3).getStates()) {
                  imageInvisable(coin2);
                }
                break;
            }
            // ... Respond to touch events
            return true;
          }
        });
  }

  private void setRightButton() {
    Button right = findViewById(R.id.right);
    right.setOnTouchListener(
        new View.OnTouchListener() {
          public boolean onTouch(View v, MotionEvent event) {
            int action = event.getActionMasked();
            switch (action) {
              case MotionEvent.ACTION_DOWN:
                heroFacingRight();
                hero.setX(manager.heroMoveRight());
                heroWalkAnimation();
              case MotionEvent.ACTION_UP:
                manager.update();
                enemy.setX(manager.Objects.get(0).getX());
                coin0.setX(manager.Objects.get(1).getX());
                coin1.setX(manager.Objects.get(2).getX());
                coin2.setX(manager.Objects.get(3).getX());
                if (!manager.Objects.get(0).getStates()) {
                  imageInvisable(enemy);
                }
                if (!manager.Objects.get(1).getStates()) {
                  imageInvisable(coin0);
                }
                if (!manager.Objects.get(2).getStates()) {
                  imageInvisable(coin1);
                }
                if (!manager.Objects.get(3).getStates()) {
                  imageInvisable(coin2);
                }

                if (((Monster) manager.Objects.get(0)).isMoveLeft()) {
                  enemyFacingLeft();
                } else {
                  enemyFacingRight();
                }

                if (((Monster) manager.Objects.get(0)).isAttack() && enemy.isShown()) {
                  enemyAttackAnimation();
                  heroHurtAnimation();

                } else {
                  enemyWalkAnimation();
                  heroWalkAnimation();
                }
                break;
            }
            // ... Respond to touch events
            return true;
          }
        });
  }

  private void setAttackButton() {
    Button attack = findViewById(R.id.attack);
    attack.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            manager.player.attack();
            heroAttackAnimation();
            manager.update();
            enemy.setX(manager.Objects.get(0).getX());
            coin0.setX(manager.Objects.get(1).getX());
            coin1.setX(manager.Objects.get(2).getX());
            coin2.setX(manager.Objects.get(3).getX());
            if (!manager.Objects.get(0).getStates()) {
              enemyHurtAnimation();
              imageInvisable(enemy);
            }
            manager.player.notAttack();
          }
        });
  }

  private void setJumpButton() {}
}

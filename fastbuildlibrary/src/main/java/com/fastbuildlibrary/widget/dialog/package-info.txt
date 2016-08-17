/*
 * FBL框架自定义对话框
 * 自定义对话框
 * 地址：https://github.com/sd6352051/NiftyDialogEffects
 *  
 * 需要依赖 compile 'com.nineoldandroids:library:2.4.0'
 *  
 *  
 * ########################################################################################
 * dependencies {
 * compile 'com.nineoldandroids:library:2.4.0'
 * compile 'com.github.sd6352051.niftydialogeffects:niftydialogeffects:1.0.0@aar'
 * }
 *  
 * 使用：
 * NiftyDialogBuilder dialogBuilder=NiftyDialogBuilder.getInstance(this);
 *  
 * dialogBuilder
 * .withTitle("Modal Dialog")
 * .withMessage("This is a modal Dialog.")
 * .show();
 *  
 * ###############################################################################
 * Configuration
 *  
 * dialogBuilder
 * .withTitle("Modal Dialog")                                  //.withTitle(null)  no title
 * .withTitleColor("#FFFFFF")                                  //def
 * .withDividerColor("#11000000")                              //def
 * .withMessage("This is a modal Dialog.")                     //.withMessage(null)  no Msg
 * .withMessageColor("#FFFFFFFF")                              //def  | withMessageColor(int resid)
 * .withDialogColor("#FFE74C3C")                               //def  | withDialogColor(int resid)
 * .withIcon(getResources().getDrawable(R.drawable.icon))
 * .withDuration(700)                                          //def
 * .withEffect(effect)                                         //def Effectstype.Slidetop
 * .withButton1Text("OK")                                      //def gone
 * .withButton2Text("Cancel")                                  //def gone
 * .isCancelableOnTouchOutside(true)                           //def    | isCancelable(true)
 * .setCustomView(R.layout.custom_view,v.getContext())         //.setCustomView(View or ResId,context)
 * .setButton1Click(new View.OnClickListener() {
 *
 * @Override public void onClick(View v) {
 * Toast.makeText(v.getContext(), "i'm btn1", Toast.LENGTH_SHORT).show();
 * }
 * })
 * .setButton2Click(new View.OnClickListener() {
 * @Override public void onClick(View v) {
 * Toast.makeText(v.getContext(),"i'm btn2",Toast.LENGTH_SHORT).show();
 * }
 * })
 * .show();
 *  
 *  
 * ################################################################################
 * 动画效果
 *  
 * Fadein, Slideleft, Slidetop, SlideBottom, Slideright, Fall, Newspager, Fliph, Flipv, RotateBottom, RotateLeft, Slit, Shake, Sidefill
 */
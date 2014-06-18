<spDocument>
   <lb name=new type=lb subtype=none>
      <av name=.gui.filename descr="No Description">
            <val value="UKIRT Standard Library">
      </av>
      <av name=title descr="No Description">
            <val value="UKIRT LIBRARY">
      </av>
      <lf name=new type=lf subtype=none>
         <av name=title descr="No Description">
                  <val value="UFTI library">
         </av>
         <av name=.gui.collapsed descr="No Description">
                  <val value="false">
         </av>
         <no name=new type=no subtype=none>
            <av name=title descr="No Description">
                        <val value="selections in this folder">
            </av>
            <av name=note descr="No Description">
                        <val value="Choose the type of UFTI observation you would like to make , copy it to\nyour Science Program and then edit the values you need to change.\n\nRemember to include an array_tests observation so that you can check the array performance at the start of each night.\n\n9 point or 5 point jitter patterns are available with standard offsets of 10 or 20 arcsecs.  (edit for non-standard offsets).\n\nQuadrant jitter patterns for deep imaging are available\n\nsky+jitter sequences are available for\naccurate photometry of faint sources\n\nA 5 x5 mosaic for an extended source is available\n\n\n">
            </av>
         </no>
         <ob name=new type=ob subtype=none>
            <av name=title descr="No Description">
                        <val value="array_tests">
            </av>
            <av name=standard descr="No Description">
                        <val value="false">
            </av>
            <av name=chainedToPrev descr="No Description">
                        <val value="false">
            </av>
            <av name=chainedToNext descr="No Description">
                        <val value="false">
            </av>
            <av name=.gui.collapsed descr="No Description">
                        <val value="true">
            </av>
            <no name=new type=no subtype=none>
               <av name=note descr="No Description">
                              <val value="The ufti configuration to use the blank filter in the component below">
               </av>
            </no>
            <oc name=new type=oc subtype=inst.UFTI>
               <av name=instPort descr="No Description">
                              <val value="East">
               </av>
               <av name=sourceMag descr="No Description">
                              <val value="none">
               </av>
               <av name=.unique descr="No Description">
                              <val value="true">
               </av>
               <av name=filter descr="No Description">
                              <val value="Blank">
               </av>
               <av name=posAngle descr="No Description">
                              <val value="0.0">
               </av>
               <av name=coadds descr="No Description">
                              <val value="2">
               </av>
               <av name=instAper descr="No Description">
                              <val value="8.13">
                              <val value="-0.23">
                              <val value="0.0">
                              <val value="2.222">
               </av>
               <av name=exposureTime descr="No Description">
                              <val value="4.0">
               </av>
               <av name=readoutArea descr="No Description">
                              <val value="1024x1024">
               </av>
               <av name=acqMode descr="No Description">
                              <val value="Normal+NDSTARE">
               </av>
               <av name=.version descr="No Description">
                              <val value="1.0">
               </av>
            </oc>
            <no name=new type=no subtype=none>
               <av name=note descr="No Description">
                              <val value="use the array tests data reduction recipe">
               </av>
            </no>
            <oc name=new type=oc subtype=DRRecipe>
               <av name=ObjectRecipe descr="No Description">
                              <val value="ARRAY_TESTS">
               </av>
               <av name=DRRecipe descr="No Description">
                              <val value="ARRAY_TESTS">
               </av>
               <av name=DarkRecipe descr="No Description">
                              <val value="ARRAY_TESTS">
               </av>
               <av name=SkyInGroup descr="No Description">
                              <val value="false">
               </av>
               <av name=.unique descr="No Description">
                              <val value="true">
               </av>
               <av name=ObjectInGroup descr="No Description">
                              <val value="false">
               </av>
               <av name=FlatRecipe descr="No Description">
                              <val value="REDUCE_FLAT">
               </av>
               <av name=DarkInGroup descr="No Description">
                              <val value="true">
               </av>
               <av name=SkyRecipe descr="No Description">
                              <val value="ARRAY_TESTS">
               </av>
               <av name=ArcRecipe descr="No Description">
                              <val value="REDUCE_ARC">
               </av>
               <av name=BiasInGroup descr="No Description">
                              <val value="false">
               </av>
               <av name=title descr="No Description">
                              <val value="ARRAY_TESTS">
               </av>
               <av name=FlatInGroup descr="No Description">
                              <val value="false">
               </av>
               <av name=BiasRecipe descr="No Description">
                              <val value="REDUCE_BIAS">
               </av>
               <av name=ArcInGroup descr="No Description">
                              <val value="false">
               </av>
            </oc>
            <if name=new type=if subtype=none>
               <no name=new type=no subtype=none>
                  <av name=note descr="No Description">
                                    <val value="take three 5 second darks, so that the first one can be thrown away">
                  </av>
               </no>
               <ic name=new type=ic subtype=darkObs>
                  <av name=coadds descr="No Description">
                                    <val value="1">
                  </av>
                  <av name=repeatCount descr="No Description">
                                    <val value="3">
                  </av>
                  <av name=exposureTime descr="No Description">
                                    <val value="5.0">
                  </av>
               </ic>
               <no name=new type=no subtype=none>
                  <av name=title descr="No Description">
                                    <val value="now do a long dark">
                  </av>
                  <av name=note descr="No Description">
                                    <val value="now do a 60 sec dark - using the dark observe iterator below to change the exposure time. ">
                  </av>
               </no>
               <ic name=new type=ic subtype=darkObs>
                  <av name=coadds descr="No Description">
                                    <val value="1">
                  </av>
                  <av name=repeatCount descr="No Description">
                                    <val value="1">
                  </av>
                  <av name=exposureTime descr="No Description">
                                    <val value="60.0">
                  </av>
               </ic>
            </if>
         </ob>
         <ob name=new type=ob subtype=none>
            <av name=title descr="No Description">
                        <val value="JHK photometry, 5pt jitters">
            </av>
            <av name=standard descr="No Description">
                        <val value="false">
            </av>
            <av name=chainedToPrev descr="No Description">
                        <val value="false">
            </av>
            <av name=chainedToNext descr="No Description">
                        <val value="false">
            </av>
            <av name=.gui.collapsed descr="No Description">
                        <val value="true">
            </av>
            <no name=new type=no subtype=none>
               <av name=title descr="No Description">
                              <val value="Useage">
               </av>
               <av name=note descr="No Description">
                              <val value="This sequence is an example of how to do generic n-colour photometry efficiently in a single observation.\n\nAlthough this is for JHK, you can change the filters to do any set.  ">
               </av>
            </no>
            <no name=new type=no subtype=none>
               <av name=title descr="No Description">
                              <val value="target information">
               </av>
               <av name=note descr="No Description">
                              <val value="enter your target co-ordinates into the target list below.  Note that although this is called a \"target list\" it really only allows you to specify ONE astronomical target\n\nWhen you have done so you can use the \"position editor\" to  select a guide star, to fine tune the co-ordinate based on a sky survey picture, examine offset locations on the field etc.\n\nFor a step by step guide to how to use the position editor use \"help\".">
               </av>
            </no>
            <oc name=new type=oc subtype=targetList>
               <av name=Base descr="No Description">
                              <val value="Base">
                              <val value="enter your object name">
                              <val value="0:00:00">
                              <val value="0:00:00">
                              <val value="FK5 (J2000)">
               </av>
               <av name=.unique descr="No Description">
                              <val value="true">
               </av>
               <av name=.gui.selectedTelescopePos descr="No Description">
                              <val value="Base">
               </av>
            </oc>
            <no name=new type=no subtype=none>
               <av name=title descr="No Description">
                              <val value="UFTI setup information">
               </av>
               <av name=note descr="No Description">
                              <val value="The UFTI component below sets to the J filter as a base configuration.\n\nThe sequence then takes darks with exposure times  appropriate to each filter that will be used.\n\nThe UFTI filters and exposure times for each filter are then \"iterated\" (cycled through) in the sequence below.\n\nThe UFTI component below has expsures set for a 11-12 mag star.   Change these as appropriate for your source.\n\n\n\n\n">
               </av>
            </no>
            <oc name=new type=oc subtype=inst.UFTI>
               <av name=instPort descr="No Description">
                              <val value="East">
               </av>
               <av name=sourceMag descr="No Description">
                              <val value="11-12">
               </av>
               <av name=.unique descr="No Description">
                              <val value="true">
               </av>
               <av name=filter descr="No Description">
                              <val value="J98">
               </av>
               <av name=posAngle descr="No Description">
                              <val value="0.0">
               </av>
               <av name=coadds descr="No Description">
                              <val value="1">
               </av>
               <av name=instAper descr="No Description">
                              <val value="-11.83">
                              <val value="16.83">
                              <val value="0.0">
                              <val value="1.250">
               </av>
               <av name=exposureTime descr="No Description">
                              <val value="15.0">
               </av>
               <av name=readoutArea descr="No Description">
                              <val value="512x512">
               </av>
               <av name=title descr="No Description">
                              <val value="- set configuration">
               </av>
               <av name=acqMode descr="No Description">
                              <val value="Normal+NDSTARE">
               </av>
               <av name=.version descr="No Description">
                              <val value="1.0">
               </av>
            </oc>
            <no name=new type=no subtype=none>
               <av name=title descr="No Description">
                              <val value="DR Recipe Selection">
               </av>
               <av name=note descr="No Description">
                              <val value="The data reduction recipe specified below uses the most basic data reduction recipe for speed.  (this is a good idea if your exposure time short).\n\nThis recipe does self flat reduction for each colour.  If you do each colour more than once then the data will be co-added for each.">
               </av>
            </no>
            <oc name=new type=oc subtype=DRRecipe>
               <av name=ObjectRecipe descr="No Description">
                              <val value="JITTER5_SELF_FLAT_NCOLOUR">
               </av>
               <av name=DRRecipe descr="No Description">
                              <val value="JITTER5_SELF_FLAT">
               </av>
               <av name=DarkRecipe descr="No Description">
                              <val value="REDUCE_DARK">
               </av>
               <av name=SkyInGroup descr="No Description">
                              <val value="true">
               </av>
               <av name=.unique descr="No Description">
                              <val value="true">
               </av>
               <av name=ObjectInGroup descr="No Description">
                              <val value="true">
               </av>
               <av name=FlatRecipe descr="No Description">
                              <val value="REDUCE_FLAT">
               </av>
               <av name=DarkInGroup descr="No Description">
                              <val value="false">
               </av>
               <av name=SkyRecipe descr="No Description">
                              <val value="REDUCE_SKY">
               </av>
               <av name=ArcRecipe descr="No Description">
                              <val value="REDUCE_ARC">
               </av>
               <av name=BiasInGroup descr="No Description">
                              <val value="false">
               </av>
               <av name=title descr="No Description">
                              <val value="JITTER5_SELF_FLAT_NCOLOUR">
               </av>
               <av name=FlatInGroup descr="No Description">
                              <val value="false">
               </av>
               <av name=BiasRecipe descr="No Description">
                              <val value="REDUCE_BIAS">
               </av>
               <av name=ArcInGroup descr="No Description">
                              <val value="false">
               </av>
            </oc>
            <if name=new type=if subtype=none>
               <av name=.gui.collapsed descr="No Description">
                              <val value="true">
               </av>
               <no name=new type=no subtype=none>
                  <av name=title descr="No Description">
                                    <val value="set dark exp time here">
                  </av>
                  <av name=note descr="No Description">
                                    <val value="If you changed the dark exposure time\nin the UFTI configuration above remember to change it here too\n\nNB :\nThe data reduction recipe will not work unless you have a dark with the right exposure time\n\nThree darks are done here - one for each filter that will follow.  Set the exposure times and co-adds to match those which you will do on the source in each band.\n">
                  </av>
               </no>
               <ic name=new type=ic subtype=darkObs>
                  <av name=coadds descr="No Description">
                                    <val value="1">
                  </av>
                  <av name=repeatCount descr="No Description">
                                    <val value="1">
                  </av>
                  <av name=exposureTime descr="No Description">
                                    <val value="15.0">
                  </av>
               </ic>
               <ic name=new type=ic subtype=darkObs>
                  <av name=coadds descr="No Description">
                                    <val value="1">
                  </av>
                  <av name=repeatCount descr="No Description">
                                    <val value="1">
                  </av>
                  <av name=exposureTime descr="No Description">
                                    <val value="10.0">
                  </av>
               </ic>
               <ic name=new type=ic subtype=darkObs>
                  <av name=coadds descr="No Description">
                                    <val value="1">
                  </av>
                  <av name=repeatCount descr="No Description">
                                    <val value="1">
                  </av>
                  <av name=exposureTime descr="No Description">
                                    <val value="10.0">
                  </av>
               </ic>
               <no name=new type=no subtype=none>
                  <av name=title descr="No Description">
                                    <val value="how to do more than one 5-point jitters">
                  </av>
                  <av name=note descr="No Description">
                                    <val value="To repeat the 5 point jitter pattern more than once for each filter on your target , use the repeat item below to specify how many times you want to repeat it.\n\n">
                  </av>
               </no>
               <ic name=new type=ic subtype=repeat>
                  <av name=repeatCount descr="No Description">
                                    <val value="1">
                  </av>
                  <av name=title descr="No Description">
                                    <val value="Repeat 1X">
                  </av>
                  <av name=.gui.collapsed descr="No Description">
                                    <val value="false">
                  </av>
                  <no name=new type=no subtype=none>
                     <av name=title descr="No Description">
                                          <val value="UFTI filter iteration">
                     </av>
                     <av name=note descr="No Description">
                                          <val value="The UFTI iteration component below changes the filter, starting with J.  No exposure time is entered for J because it is already set in the\ngeneral UFTI setup above.   The fitler and exposure time are then changed for H and then again for K.">
                     </av>
                  </no>
                  <ic name=new type=ic subtype=instUFTI>
                     <av name=filterIter descr="No Description">
                                          <val value="J98">
                                          <val value="H98">
                                          <val value="K98">
                     </av>
                     <av name=instAperLIter descr="No Description">
                                          <val value="1.250">
                                          <val value="1.635">
                                          <val value="2.150">
                     </av>
                     <av name=exposureTimeIter descr="No Description">
                                          <val value="">
                                          <val value="10">
                                          <val value="10">
                     </av>
                     <av name=iterConfigList descr="No Description">
                                          <val value="filterIter">
                                          <val value="instAperLIter">
                                          <val value="exposureTimeIter">
                     </av>
                     <no name=new type=no subtype=none>
                        <av name=title descr="No Description">
                                                <val value="offset info">
                        </av>
                        <av name=note descr="No Description">
                                                <val value="the non-square pattern of offsets in the jitter below avoids re-inforcement of poor row/column\n\nIf you want to use a different size of offset, move the highlight to the offset\nitem below and edit them.">
                        </av>
                     </no>
                     <ic name=new type=ic subtype=offset>
                        <av name=Offset8 descr="No Description">
                                                <val value="0.0">
                                                <val value="0.0">
                        </av>
                        <av name=Offset7 descr="No Description">
                                                <val value="11.0">
                                                <val value="-11.0">
                        </av>
                        <av name=Offset5 descr="No Description">
                                                <val value="-12.0">
                                                <val value="-10.0">
                        </av>
                        <av name=Offset3 descr="No Description">
                                                <val value="-10.0">
                                                <val value="11.0">
                        </av>
                        <av name=title descr="No Description">
                                                <val value="jitter_5_10as">
                        </av>
                        <av name=.gui.selectedOffsetPos descr="No Description">
                                                <val value="Offset8">
                        </av>
                        <av name=Offset1 descr="No Description">
                                                <val value="11.0">
                                                <val value="10.0">
                        </av>
                        <av name=offsetPositions descr="No Description">
                                                <val value="Offset8">
                                                <val value="Offset3">
                                                <val value="Offset5">
                                                <val value="Offset7">
                                                <val value="Offset1">
                        </av>
                        <av name=.gui.collapsed descr="No Description">
                                                <val value="false">
                        </av>
                        <ic name=new type=ic subtype=observe>
                           <av name=repeatCount descr="No Description">
                                                      <val value="1">
                           </av>
                        </ic>
                     </ic>
                  </ic>
               </ic>
               <no name=new type=no subtype=none>
                  <av name=title descr="No Description">
                                    <val value="last offset">
                  </av>
                  <av name=note descr="No Description">
                                    <val value="This last offset simply takes the telescope back to 00 after all the repeats of the jitter pattern are finished">
                  </av>
               </no>
               <ic name=new type=ic subtype=offset>
                  <av name=offsetPositions descr="No Description">
                                    <val value="Offset0">
                  </av>
                  <av name=Offset0 descr="No Description">
                                    <val value="0.0">
                                    <val value="0.0">
                  </av>
                  <av name=.gui.selectedOffsetPos descr="No Description">
                                    <val value="Offset0">
                  </av>
               </ic>
            </if>
         </ob>
         <ob name=new type=ob subtype=none>
            <av name=title descr="No Description">
                        <val value="9pt jitter/offsets of 20arcsec">
            </av>
            <av name=standard descr="No Description">
                        <val value="false">
            </av>
            <av name=chainedToPrev descr="No Description">
                        <val value="false">
            </av>
            <av name=chainedToNext descr="No Description">
                        <val value="false">
            </av>
            <av name=.gui.collapsed descr="No Description">
                        <val value="true">
            </av>
            <no name=new type=no subtype=none>
               <av name=title descr="No Description">
                              <val value="target information">
               </av>
               <av name=note descr="No Description">
                              <val value="enter your target co-ordinates into the target list below.  Note that although this is called a \"target list\" it only allows you to specify ONE astronomical target\n\nWhen you have done so you can use the \"position editor\" to  select a guide star, to fine tune the co-ordinate based on a sky survey picture, examine offset locations on the field etc.\n\nFor a step by step guide to how to use the position editor use \"help\".">
               </av>
            </no>
            <oc name=new type=oc subtype=targetList>
               <av name=Base descr="No Description">
                              <val value="Base">
                              <val value="enter your object name">
                              <val value="0:00:00">
                              <val value="0:00:00">
                              <val value="FK5 (J2000)">
               </av>
               <av name=.unique descr="No Description">
                              <val value="true">
               </av>
               <av name=.gui.selectedTelescopePos descr="No Description">
                              <val value="Base">
               </av>
            </oc>
            <no name=new type=no subtype=none>
               <av name=title descr="No Description">
                              <val value="how to set the exposure time and filter">
               </av>
               <av name=note descr="No Description">
                              <val value="The UFTI component below uses the K filter.  To change it move the yellow highlight to the \"ufti component\" below and select the filter you want to use.\n\nThe UFTI component below uses a 60 second expsoure time.   To change it move the highlight to \"ufti\" below and change the exposure time - either enter the value you want to use, or enter the magnitude range of your target and a recommended exposure time will be automatically entered.\n\n">
               </av>
            </no>
            <oc name=new type=oc subtype=inst.UFTI>
               <av name=instPort descr="No Description">
                              <val value="East">
               </av>
               <av name=sourceMag descr="No Description">
                              <val value="BL">
               </av>
               <av name=.unique descr="No Description">
                              <val value="true">
               </av>
               <av name=filter descr="No Description">
                              <val value="K98">
               </av>
               <av name=posAngle descr="No Description">
                              <val value="0.0">
               </av>
               <av name=coadds descr="No Description">
                              <val value="1">
               </av>
               <av name=instAper descr="No Description">
                              <val value="0.998">
                              <val value="-0.62">
                              <val value="0.0">
                              <val value="2.150">
               </av>
               <av name=exposureTime descr="No Description">
                              <val value="60.0">
               </av>
               <av name=readoutArea descr="No Description">
                              <val value="1024x1024">
               </av>
               <av name=title descr="No Description">
                              <val value="- set configuration">
               </av>
               <av name=acqMode descr="No Description">
                              <val value="Normal+NDSTARE">
               </av>
               <av name=.version descr="No Description">
                              <val value="1.0">
               </av>
            </oc>
            <no name=new type=no subtype=none>
               <av name=title descr="No Description">
                              <val value="DR Recipe Selection">
               </av>
               <av name=note descr="No Description">
                              <val value="The data reduction recipe specified below uses the most complete and slowest data reduction script \n\nIf you need to use a faster recipe then\nselect JITTER9_SELF_FLAT_BASIC (this\nis a good idea if your exposure time is\nshorter than 60 secs)">
               </av>
            </no>
            <oc name=new type=oc subtype=DRRecipe>
               <av name=ObjectRecipe descr="No Description">
                              <val value="JITTER9_SELF_FLAT">
               </av>
               <av name=DRRecipe descr="No Description">
                              <val value="JITTER9_SELF_FLAT">
               </av>
               <av name=DarkRecipe descr="No Description">
                              <val value="REDUCE_DARK">
               </av>
               <av name=SkyInGroup descr="No Description">
                              <val value="false">
               </av>
               <av name=.unique descr="No Description">
                              <val value="true">
               </av>
               <av name=ObjectInGroup descr="No Description">
                              <val value="true">
               </av>
               <av name=FlatRecipe descr="No Description">
                              <val value="REDUCE_FLAT">
               </av>
               <av name=DarkInGroup descr="No Description">
                              <val value="false">
               </av>
               <av name=SkyRecipe descr="No Description">
                              <val value="REDUCE_SKY">
               </av>
               <av name=ArcRecipe descr="No Description">
                              <val value="REDUCE_ARC">
               </av>
               <av name=BiasInGroup descr="No Description">
                              <val value="false">
               </av>
               <av name=title descr="No Description">
                              <val value="JITTER9_SELF_FLAT">
               </av>
               <av name=FlatInGroup descr="No Description">
                              <val value="false">
               </av>
               <av name=BiasRecipe descr="No Description">
                              <val value="REDUCE_BIAS">
               </av>
               <av name=ArcInGroup descr="No Description">
                              <val value="false">
               </av>
            </oc>
            <if name=new type=if subtype=none>
               <av name=.gui.collapsed descr="No Description">
                              <val value="false">
               </av>
               <no name=new type=no subtype=none>
                  <av name=title descr="No Description">
                                    <val value="change exp time here">
                  </av>
                  <av name=note descr="No Description">
                                    <val value="If you changed the exposure time in the UFTI configuration, remember to \nchange it for the dark.\n\nthe data reduction recipe will not work unless you have a dark with the right exposure time\n\nClick on the reset to default button to set the exposure time equal to that in the ufti component above.">
                  </av>
               </no>
               <ic name=new type=ic subtype=darkObs>
                  <av name=coadds descr="No Description">
                                    <val value="1">
                  </av>
                  <av name=repeatCount descr="No Description">
                                    <val value="1">
                  </av>
                  <av name=exposureTime descr="No Description">
                                    <val value="60.0">
                  </av>
               </ic>
               <no name=new type=no subtype=none>
                  <av name=title descr="No Description">
                                    <val value="how to do more than one 9-point jitters">
                  </av>
                  <av name=note descr="No Description">
                                    <val value="To repeat the 9 point jitter pattern more than once on your target , use the repeat item below to specify how many times you want to repeat it.\n\n">
                  </av>
               </no>
               <ic name=new type=ic subtype=repeat>
                  <av name=.gui.collapsed descr="No Description">
                                    <val value="false">
                  </av>
                  <no name=new type=no subtype=none>
                     <av name=title descr="No Description">
                                          <val value="offset info">
                     </av>
                     <av name=note descr="No Description">
                                          <val value="the non-square pattern of offsets in the jitter below avoids re-inforcement of poor row/column\n\nIf you want to use a different size of offset, move the highlight to the offset\nitem below and edit the offset values.">
                     </av>
                  </no>
                  <ic name=new type=ic subtype=offset>
                     <av name=Offset8 descr="No Description">
                                          <val value="2.0">
                                          <val value="-21.0">
                     </av>
                     <av name=Offset7 descr="No Description">
                                          <val value="22.0">
                                          <val value="-22.0">
                     </av>
                     <av name=Offset6 descr="No Description">
                                          <val value="-21.0">
                                          <val value="2.0">
                     </av>
                     <av name=Offset5 descr="No Description">
                                          <val value="-22.0">
                                          <val value="-20.0">
                     </av>
                     <av name=Offset4 descr="No Description">
                                          <val value="1.0">
                                          <val value="21.0">
                     </av>
                     <av name=Offset3 descr="No Description">
                                          <val value="-20.0">
                                          <val value="22.0">
                     </av>
                     <av name=Offset2 descr="No Description">
                                          <val value="20.0">
                                          <val value="1.0">
                     </av>
                     <av name=title descr="No Description">
                                          <val value="9 pt jitter">
                     </av>
                     <av name=.gui.selectedOffsetPos descr="No Description">
                                          <val value="Offset0">
                     </av>
                     <av name=Offset1 descr="No Description">
                                          <val value="21.0">
                                          <val value="20.0">
                     </av>
                     <av name=Offset0 descr="No Description">
                                          <val value="0.0">
                                          <val value="0.0">
                     </av>
                     <av name=offsetPositions descr="No Description">
                                          <val value="Offset0">
                                          <val value="Offset2">
                                          <val value="Offset1">
                                          <val value="Offset4">
                                          <val value="Offset3">
                                          <val value="Offset6">
                                          <val value="Offset5">
                                          <val value="Offset8">
                                          <val value="Offset7">
                     </av>
                     <av name=.gui.collapsed descr="No Description">
                                          <val value="false">
                     </av>
                     <ic name=new type=ic subtype=observe>
                        <av name=repeatCount descr="No Description">
                                                <val value="1">
                        </av>
                     </ic>
                  </ic>
               </ic>
               <no name=new type=no subtype=none>
                  <av name=title descr="No Description">
                                    <val value="offset back to 00">
                  </av>
                  <av name=note descr="No Description">
                                    <val value="This final offset takes the telescope back to the\nstart position (00) after all the data has been obtained.">
                  </av>
               </no>
               <ic name=new type=ic subtype=offset>
                  <av name=offsetPositions descr="No Description">
                                    <val value="Offset0">
                  </av>
                  <av name=Offset0 descr="No Description">
                                    <val value="0.0">
                                    <val value="0.0">
                  </av>
                  <av name=.gui.selectedOffsetPos descr="No Description">
                                    <val value="Offset0">
                  </av>
               </ic>
            </if>
         </ob>
         <ob name=new type=ob subtype=none>
            <av name=title descr="No Description">
                        <val value="9point jitter/offsets of 10arcsec">
            </av>
            <av name=standard descr="No Description">
                        <val value="false">
            </av>
            <av name=chainedToPrev descr="No Description">
                        <val value="false">
            </av>
            <av name=chainedToNext descr="No Description">
                        <val value="false">
            </av>
            <av name=.gui.collapsed descr="No Description">
                        <val value="true">
            </av>
            <no name=new type=no subtype=none>
               <av name=title descr="No Description">
                              <val value="target information">
               </av>
               <av name=note descr="No Description">
                              <val value="enter your target co-ordinates into the target list below.  Note that although this is called a \"target list\" it really only allows you to specify ONE astronomical target\n\nWhen you have done so you can use the \"position editor\" to  select a guide star, to fine tune the co-ordinate based on a sky survey picture, examine offset locations on the field etc.\n\nFor a step by step guide to how to use the position editor use \"help\".">
               </av>
            </no>
            <oc name=new type=oc subtype=targetList>
               <av name=Base descr="No Description">
                              <val value="Base">
                              <val value="enter your object name">
                              <val value="0:00:00">
                              <val value="0:00:00">
                              <val value="FK5 (J2000)">
               </av>
               <av name=.unique descr="No Description">
                              <val value="true">
               </av>
               <av name=.gui.selectedTelescopePos descr="No Description">
                              <val value="Base">
               </av>
            </oc>
            <no name=new type=no subtype=none>
               <av name=title descr="No Description">
                              <val value="how to set the exposure time and filter">
               </av>
               <av name=note descr="No Description">
                              <val value="The UFTI component below uses the K filter.  To change it move the highlight to the \"ufti component\" below and select the filter you want to use.\n\nThe UFTI component below uses a 60 second expsoure time.   To change it move the highlight to \"ufti\" below and change the exposure time - either enter the value you want to use, or enter the magnitude range of your target and the recommended exposure time will be automatically entered.\n\n">
               </av>
            </no>
            <oc name=new type=oc subtype=inst.UFTI>
               <av name=instPort descr="No Description">
                              <val value="East">
               </av>
               <av name=sourceMag descr="No Description">
                              <val value="BL">
               </av>
               <av name=.unique descr="No Description">
                              <val value="true">
               </av>
               <av name=filter descr="No Description">
                              <val value="K98">
               </av>
               <av name=posAngle descr="No Description">
                              <val value="0.0">
               </av>
               <av name=coadds descr="No Description">
                              <val value="1">
               </av>
               <av name=instAper descr="No Description">
                              <val value="0.998">
                              <val value="-0.62">
                              <val value="0.0">
                              <val value="2.150">
               </av>
               <av name=exposureTime descr="No Description">
                              <val value="60.0">
               </av>
               <av name=readoutArea descr="No Description">
                              <val value="1024x1024">
               </av>
               <av name=title descr="No Description">
                              <val value="- set configuration">
               </av>
               <av name=acqMode descr="No Description">
                              <val value="Normal+NDSTARE">
               </av>
               <av name=.version descr="No Description">
                              <val value="1.0">
               </av>
            </oc>
            <no name=new type=no subtype=none>
               <av name=title descr="No Description">
                              <val value="DR Recipe Selection">
               </av>
               <av name=note descr="No Description">
                              <val value="The data reduction recipe specified below uses the most basic data reduction recipe for speed.  (thisis a good idea if your exposure time short).\n\nIf you need to use a more complete recipe then select JITTER9_SELF_FLAT\n">
               </av>
            </no>
            <oc name=new type=oc subtype=DRRecipe>
               <av name=ObjectRecipe descr="No Description">
                              <val value="JITTER9_SELF_FLAT_NO_MASK">
               </av>
               <av name=DRRecipe descr="No Description">
                              <val value="JITTER9_SELF_FLAT_BASIC">
               </av>
               <av name=DarkRecipe descr="No Description">
                              <val value="REDUCE_DARK">
               </av>
               <av name=SkyInGroup descr="No Description">
                              <val value="false">
               </av>
               <av name=.unique descr="No Description">
                              <val value="true">
               </av>
               <av name=ObjectInGroup descr="No Description">
                              <val value="true">
               </av>
               <av name=FlatRecipe descr="No Description">
                              <val value="REDUCE_FLAT">
               </av>
               <av name=DarkInGroup descr="No Description">
                              <val value="false">
               </av>
               <av name=SkyRecipe descr="No Description">
                              <val value="REDUCE_SKY">
               </av>
               <av name=ArcRecipe descr="No Description">
                              <val value="REDUCE_ARC">
               </av>
               <av name=BiasInGroup descr="No Description">
                              <val value="false">
               </av>
               <av name=title descr="No Description">
                              <val value="JITTER9_SELF_FLAT_NO_MASK">
               </av>
               <av name=FlatInGroup descr="No Description">
                              <val value="false">
               </av>
               <av name=BiasRecipe descr="No Description">
                              <val value="REDUCE_BIAS">
               </av>
               <av name=ArcInGroup descr="No Description">
                              <val value="false">
               </av>
            </oc>
            <if name=new type=if subtype=none>
               <av name=.gui.collapsed descr="No Description">
                              <val value="false">
               </av>
               <no name=new type=no subtype=none>
                  <av name=title descr="No Description">
                                    <val value="change exp time here too">
                  </av>
                  <av name=note descr="No Description">
                                    <val value="If you changed the exposure time in\nthe UFTI configuration then remember to change it for the dark too\n\nNB :\nThe data reduction recipe will not work unless you have a dark with the right exposure time\n\nUse the reset to defaults button to set the dark exposure time equal to the value from the component above">
                  </av>
               </no>
               <ic name=new type=ic subtype=darkObs>
                  <av name=coadds descr="No Description">
                                    <val value="1">
                  </av>
                  <av name=repeatCount descr="No Description">
                                    <val value="1">
                  </av>
                  <av name=exposureTime descr="No Description">
                                    <val value="60.0">
                  </av>
               </ic>
               <no name=new type=no subtype=none>
                  <av name=title descr="No Description">
                                    <val value="how to do more than one 9-point jitters">
                  </av>
                  <av name=note descr="No Description">
                                    <val value="To repeat the 9 point jitter pattern more than once on your target , use the repeat item below to specify how many times you want to repeat it.\n\n">
                  </av>
               </no>
               <ic name=new type=ic subtype=repeat>
                  <av name=repeatCount descr="No Description">
                                    <val value="1">
                  </av>
                  <av name=title descr="No Description">
                                    <val value="Repeat 1X">
                  </av>
                  <av name=.gui.collapsed descr="No Description">
                                    <val value="false">
                  </av>
                  <no name=new type=no subtype=none>
                     <av name=title descr="No Description">
                                          <val value="offset info">
                     </av>
                     <av name=note descr="No Description">
                                          <val value="the non-square pattern of offsets in the jitter below avoids re-inforcement of poor row/column\n\nIf you want to use a different size of offset, move the highlight to the offset\nitem below and edit them.">
                     </av>
                  </no>
                  <ic name=new type=ic subtype=offset>
                     <av name=Offset8 descr="No Description">
                                          <val value="0.0">
                                          <val value="0.0">
                     </av>
                     <av name=Offset7 descr="No Description">
                                          <val value="12.0">
                                          <val value="-12.0">
                     </av>
                     <av name=Offset6 descr="No Description">
                                          <val value="2.0">
                                          <val value="-11.0">
                     </av>
                     <av name=Offset5 descr="No Description">
                                          <val value="-12.0">
                                          <val value="-10.0">
                     </av>
                     <av name=Offset4 descr="No Description">
                                          <val value="-11.0">
                                          <val value="2.0">
                     </av>
                     <av name=Offset3 descr="No Description">
                                          <val value="-10.0">
                                          <val value="12.0">
                     </av>
                     <av name=.gui.selectedOffsetPos descr="No Description">
                                          <val value="Offset8">
                     </av>
                     <av name=title descr="No Description">
                                          <val value="jitter_9_10as">
                     </av>
                     <av name=Offset2 descr="No Description">
                                          <val value="1.0">
                                          <val value="11.0">
                     </av>
                     <av name=Offset1 descr="No Description">
                                          <val value="11.0">
                                          <val value="10.0">
                     </av>
                     <av name=Offset0 descr="No Description">
                                          <val value="10.0">
                                          <val value="1.0">
                     </av>
                     <av name=offsetPositions descr="No Description">
                                          <val value="Offset8">
                                          <val value="Offset0">
                                          <val value="Offset1">
                                          <val value="Offset2">
                                          <val value="Offset3">
                                          <val value="Offset4">
                                          <val value="Offset5">
                                          <val value="Offset6">
                                          <val value="Offset7">
                     </av>
                     <av name=.gui.collapsed descr="No Description">
                                          <val value="false">
                     </av>
                     <ic name=new type=ic subtype=observe>
                        <av name=repeatCount descr="No Description">
                                                <val value="1">
                        </av>
                     </ic>
                  </ic>
               </ic>
               <no name=new type=no subtype=none>
                  <av name=title descr="No Description">
                                    <val value="last offset">
                  </av>
                  <av name=note descr="No Description">
                                    <val value="This last offset ensures the telescope moves back\nto 00 after all the observations are completed.">
                  </av>
               </no>
               <ic name=new type=ic subtype=offset>
                  <av name=offsetPositions descr="No Description">
                                    <val value="Offset0">
                  </av>
                  <av name=Offset0 descr="No Description">
                                    <val value="0.0">
                                    <val value="0.0">
                  </av>
                  <av name=.gui.selectedOffsetPos descr="No Description">
                                    <val value="Offset0">
                  </av>
               </ic>
            </if>
         </ob>
         <ob name=new type=ob subtype=none>
            <av name=title descr="No Description">
                        <val value="5pt jitter/offsets of 10arcsec">
            </av>
            <av name=standard descr="No Description">
                        <val value="false">
            </av>
            <av name=chainedToPrev descr="No Description">
                        <val value="false">
            </av>
            <av name=chainedToNext descr="No Description">
                        <val value="false">
            </av>
            <av name=.gui.collapsed descr="No Description">
                        <val value="true">
            </av>
            <no name=new type=no subtype=none>
               <av name=title descr="No Description">
                              <val value="target information">
               </av>
               <av name=note descr="No Description">
                              <val value="enter your target co-ordinates into the target list below.  Note that although this is called a \"target list\" it really only allows you to specify ONE astronomical target\n\nWhen you have done so you can use the \"position editor\" to  select a guide star, to fine tune the co-ordinate based on a sky survey picture, examine offset locations on the field etc.\n\nFor a step by step guide to how to use the position editor use \"help\".">
               </av>
            </no>
            <oc name=new type=oc subtype=targetList>
               <av name=Base descr="No Description">
                              <val value="Base">
                              <val value="enter your object name">
                              <val value="0:00:00">
                              <val value="0:00:00">
                              <val value="FK5 (J2000)">
               </av>
               <av name=.unique descr="No Description">
                              <val value="true">
               </av>
               <av name=.gui.selectedTelescopePos descr="No Description">
                              <val value="Base">
               </av>
            </oc>
            <no name=new type=no subtype=none>
               <av name=title descr="No Description">
                              <val value="how to set the exposure time and filter">
               </av>
               <av name=note descr="No Description">
                              <val value="The UFTI component below uses the K filter.  To change it move the highlight to the \"ufti component\" below and select the filter you want to use.\n\nThe UFTI component below uses a 60 second expsoure time.   To change it move the highlight to \"ufti\" below and change the exposure time - either enter the value you want to use, or enter the magnitude range of your target and the recommended exposure time will be automatically entered.\n\n">
               </av>
            </no>
            <oc name=new type=oc subtype=inst.UFTI>
               <av name=instPort descr="No Description">
                              <val value="East">
               </av>
               <av name=sourceMag descr="No Description">
                              <val value="BL">
               </av>
               <av name=.unique descr="No Description">
                              <val value="true">
               </av>
               <av name=filter descr="No Description">
                              <val value="K98">
               </av>
               <av name=posAngle descr="No Description">
                              <val value="0.0">
               </av>
               <av name=coadds descr="No Description">
                              <val value="1">
               </av>
               <av name=instAper descr="No Description">
                              <val value="0.998">
                              <val value="-0.62">
                              <val value="0.0">
                              <val value="2.150">
               </av>
               <av name=exposureTime descr="No Description">
                              <val value="60.0">
               </av>
               <av name=readoutArea descr="No Description">
                              <val value="1024x1024">
               </av>
               <av name=title descr="No Description">
                              <val value="- set configuration">
               </av>
               <av name=acqMode descr="No Description">
                              <val value="Normal+NDSTARE">
               </av>
               <av name=.version descr="No Description">
                              <val value="1.0">
               </av>
            </oc>
            <no name=new type=no subtype=none>
               <av name=title descr="No Description">
                              <val value="DR Recipe Selection">
               </av>
               <av name=note descr="No Description">
                              <val value="The data reduction recipe specified below uses the most basic data reduction recipe for speed.  (this is a good idea if your exposure time short).\n\nIf you need to use a more complete recipe then select JITTER5_SELF_FLAT_APHOT\n">
               </av>
            </no>
            <oc name=new type=oc subtype=DRRecipe>
               <av name=ObjectRecipe descr="No Description">
                              <val value="JITTER5_SELF_FLAT">
               </av>
               <av name=DRRecipe descr="No Description">
                              <val value="JITTER5_SELF_FLAT">
               </av>
               <av name=DarkRecipe descr="No Description">
                              <val value="REDUCE_DARK">
               </av>
               <av name=SkyInGroup descr="No Description">
                              <val value="true">
               </av>
               <av name=.unique descr="No Description">
                              <val value="true">
               </av>
               <av name=ObjectInGroup descr="No Description">
                              <val value="true">
               </av>
               <av name=FlatRecipe descr="No Description">
                              <val value="REDUCE_FLAT">
               </av>
               <av name=DarkInGroup descr="No Description">
                              <val value="false">
               </av>
               <av name=SkyRecipe descr="No Description">
                              <val value="REDUCE_SKY">
               </av>
               <av name=ArcRecipe descr="No Description">
                              <val value="REDUCE_ARC">
               </av>
               <av name=BiasInGroup descr="No Description">
                              <val value="false">
               </av>
               <av name=title descr="No Description">
                              <val value="JITTER5_SELF_FLAT">
               </av>
               <av name=FlatInGroup descr="No Description">
                              <val value="false">
               </av>
               <av name=BiasRecipe descr="No Description">
                              <val value="REDUCE_BIAS">
               </av>
               <av name=ArcInGroup descr="No Description">
                              <val value="false">
               </av>
            </oc>
            <if name=new type=if subtype=none>
               <av name=.gui.collapsed descr="No Description">
                              <val value="false">
               </av>
               <no name=new type=no subtype=none>
                  <av name=title descr="No Description">
                                    <val value="set dark exp time here">
                  </av>
                  <av name=note descr="No Description">
                                    <val value="If you changed the dark exposure time\nin the UFTI configuration aobe remember to change it here too\n\nNB :\nThe data reduction recipe will not work unless you have a dark with the right exposure time\n\nUse the reset to default button to ensure the dark exposure time matches that in your UFTI setting above,">
                  </av>
               </no>
               <ic name=new type=ic subtype=darkObs>
                  <av name=coadds descr="No Description">
                                    <val value="1">
                  </av>
                  <av name=repeatCount descr="No Description">
                                    <val value="1">
                  </av>
                  <av name=exposureTime descr="No Description">
                                    <val value="60.0">
                  </av>
               </ic>
               <no name=new type=no subtype=none>
                  <av name=title descr="No Description">
                                    <val value="how to do more than one 5-point jitters">
                  </av>
                  <av name=note descr="No Description">
                                    <val value="To repeat the 5 point jitter pattern more than once on your target , use the repeat item below to specify how many times you want to repeat it.\n\n">
                  </av>
               </no>
               <ic name=new type=ic subtype=repeat>
                  <av name=.gui.collapsed descr="No Description">
                                    <val value="false">
                  </av>
                  <no name=new type=no subtype=none>
                     <av name=title descr="No Description">
                                          <val value="offset info">
                     </av>
                     <av name=note descr="No Description">
                                          <val value="the non-square pattern of offsets in the jitter below avoids re-inforcement of poor row/column\n\nIf you want to use a different size of offset, move the highlight to the offset\nitem below and edit them.">
                     </av>
                  </no>
                  <ic name=new type=ic subtype=offset>
                     <av name=Offset8 descr="No Description">
                                          <val value="0.0">
                                          <val value="0.0">
                     </av>
                     <av name=Offset7 descr="No Description">
                                          <val value="11.0">
                                          <val value="-11.0">
                     </av>
                     <av name=Offset5 descr="No Description">
                                          <val value="-12.0">
                                          <val value="-10.0">
                     </av>
                     <av name=Offset3 descr="No Description">
                                          <val value="-10.0">
                                          <val value="11.0">
                     </av>
                     <av name=title descr="No Description">
                                          <val value="jitter_5_10as">
                     </av>
                     <av name=.gui.selectedOffsetPos descr="No Description">
                                          <val value="Offset8">
                     </av>
                     <av name=Offset1 descr="No Description">
                                          <val value="11.0">
                                          <val value="10.0">
                     </av>
                     <av name=offsetPositions descr="No Description">
                                          <val value="Offset8">
                                          <val value="Offset3">
                                          <val value="Offset5">
                                          <val value="Offset7">
                                          <val value="Offset1">
                     </av>
                     <av name=.gui.collapsed descr="No Description">
                                          <val value="false">
                     </av>
                     <ic name=new type=ic subtype=observe>
                        <av name=repeatCount descr="No Description">
                                                <val value="1">
                        </av>
                     </ic>
                  </ic>
               </ic>
               <no name=new type=no subtype=none>
                  <av name=title descr="No Description">
                                    <val value="last offset">
                  </av>
                  <av name=note descr="No Description">
                                    <val value="This last offset simply takes the telescope back to 00 after all the repeats of the jitter pattern are finished">
                  </av>
               </no>
               <ic name=new type=ic subtype=offset>
                  <av name=offsetPositions descr="No Description">
                                    <val value="Offset0">
                  </av>
                  <av name=Offset0 descr="No Description">
                                    <val value="0.0">
                                    <val value="0.0">
                  </av>
                  <av name=.gui.selectedOffsetPos descr="No Description">
                                    <val value="Offset0">
                  </av>
               </ic>
            </if>
         </ob>
         <ob name=new type=ob subtype=none>
            <av name=title descr="No Description">
                        <val value="5pt jitter/offsets of 20arcsec">
            </av>
            <av name=standard descr="No Description">
                        <val value="false">
            </av>
            <av name=chainedToPrev descr="No Description">
                        <val value="false">
            </av>
            <av name=chainedToNext descr="No Description">
                        <val value="false">
            </av>
            <av name=.gui.collapsed descr="No Description">
                        <val value="true">
            </av>
            <no name=new type=no subtype=none>
               <av name=title descr="No Description">
                              <val value="target information">
               </av>
               <av name=note descr="No Description">
                              <val value="enter your target co-ordinates into the target list below.  Note that although this is called a \"target list\" it really only allows you to specify ONE astronomical target\n\nWhen you have done so you can use the \"position editor\" to  select a guide star, to fine tune the co-ordinate based on a sky survey picture, examine offset locations on the field etc.\n\nFor a step by step guide to how to use the position editor use \"help\".">
               </av>
            </no>
            <oc name=new type=oc subtype=targetList>
               <av name=Base descr="No Description">
                              <val value="Base">
                              <val value="enter your object name">
                              <val value="0:00:00">
                              <val value="0:00:00">
                              <val value="FK5 (J2000)">
               </av>
               <av name=.unique descr="No Description">
                              <val value="true">
               </av>
               <av name=.gui.selectedTelescopePos descr="No Description">
                              <val value="Base">
               </av>
            </oc>
            <no name=new type=no subtype=none>
               <av name=title descr="No Description">
                              <val value="how to set the exposure time and filter">
               </av>
               <av name=note descr="No Description">
                              <val value="The UFTI component below uses the K filter.  To change it move the highlight to the \"ufti component\" below and select the filter you want to use.\n\nThe UFTI component below uses a 60 second expsoure time.   To change it move the highlight to \"ufti\" below and change the exposure time - either enter the value you want to use, or enter the magnitude range of your target and the recommended exposure time will be automatically entered.\n\n">
               </av>
            </no>
            <oc name=new type=oc subtype=inst.UFTI>
               <av name=instPort descr="No Description">
                              <val value="East">
               </av>
               <av name=sourceMag descr="No Description">
                              <val value="BL">
               </av>
               <av name=.unique descr="No Description">
                              <val value="true">
               </av>
               <av name=filter descr="No Description">
                              <val value="K98">
               </av>
               <av name=posAngle descr="No Description">
                              <val value="0.0">
               </av>
               <av name=coadds descr="No Description">
                              <val value="1">
               </av>
               <av name=instAper descr="No Description">
                              <val value="0.998">
                              <val value="-0.62">
                              <val value="0.0">
                              <val value="2.150">
               </av>
               <av name=exposureTime descr="No Description">
                              <val value="60.0">
               </av>
               <av name=readoutArea descr="No Description">
                              <val value="1024x1024">
               </av>
               <av name=title descr="No Description">
                              <val value="- set configuration">
               </av>
               <av name=acqMode descr="No Description">
                              <val value="Normal+NDSTARE">
               </av>
               <av name=.version descr="No Description">
                              <val value="1.0">
               </av>
            </oc>
            <no name=new type=no subtype=none>
               <av name=title descr="No Description">
                              <val value="DR Recipe Selection">
               </av>
               <av name=note descr="No Description">
                              <val value="The data reduction recipe specified below uses the most basic data reduction recipe for speed.  (this is a good idea if your exposure time short).\n\nIf you need to use a more complete recipe then select JITTER5_SELF_FLAT_APHOT\nwhich will also do aperture photometry">
               </av>
            </no>
            <oc name=new type=oc subtype=DRRecipe>
               <av name=ObjectRecipe descr="No Description">
                              <val value="JITTER5_SELF_FLAT">
               </av>
               <av name=DRRecipe descr="No Description">
                              <val value="JITTER5_SELF_FLAT">
               </av>
               <av name=DarkRecipe descr="No Description">
                              <val value="REDUCE_DARK">
               </av>
               <av name=SkyInGroup descr="No Description">
                              <val value="true">
               </av>
               <av name=.unique descr="No Description">
                              <val value="true">
               </av>
               <av name=ObjectInGroup descr="No Description">
                              <val value="true">
               </av>
               <av name=FlatRecipe descr="No Description">
                              <val value="REDUCE_FLAT">
               </av>
               <av name=DarkInGroup descr="No Description">
                              <val value="false">
               </av>
               <av name=SkyRecipe descr="No Description">
                              <val value="REDUCE_SKY">
               </av>
               <av name=ArcRecipe descr="No Description">
                              <val value="REDUCE_ARC">
               </av>
               <av name=BiasInGroup descr="No Description">
                              <val value="false">
               </av>
               <av name=title descr="No Description">
                              <val value="JITTER5_SELF_FLAT">
               </av>
               <av name=FlatInGroup descr="No Description">
                              <val value="false">
               </av>
               <av name=BiasRecipe descr="No Description">
                              <val value="REDUCE_BIAS">
               </av>
               <av name=ArcInGroup descr="No Description">
                              <val value="false">
               </av>
            </oc>
            <if name=new type=if subtype=none>
               <av name=.gui.collapsed descr="No Description">
                              <val value="false">
               </av>
               <no name=new type=no subtype=none>
                  <av name=title descr="No Description">
                                    <val value="change dark exp time here">
                  </av>
                  <av name=note descr="No Description">
                                    <val value="If you changed the exposure time in the UFTI configuration above, then you should change it for the dark too\n\nNB :\nThe data reduction recipe will not work unless you have a dark with the right exposure time\n\nUse the reset to default button to ensure the dark expsure times for your source and dark are matched">
                  </av>
               </no>
               <ic name=new type=ic subtype=darkObs>
                  <av name=coadds descr="No Description">
                                    <val value="1">
                  </av>
                  <av name=repeatCount descr="No Description">
                                    <val value="1">
                  </av>
                  <av name=exposureTime descr="No Description">
                                    <val value="60.0">
                  </av>
               </ic>
               <no name=new type=no subtype=none>
                  <av name=title descr="No Description">
                                    <val value="how to do more than one 5-point jitters">
                  </av>
                  <av name=note descr="No Description">
                                    <val value="To repeat the 5 point jitter pattern more than once on your target , use the repeat item below to specify how many times you want to repeat it.\n\n">
                  </av>
               </no>
               <ic name=new type=ic subtype=repeat>
                  <av name=.gui.collapsed descr="No Description">
                                    <val value="false">
                  </av>
                  <no name=new type=no subtype=none>
                     <av name=title descr="No Description">
                                          <val value="offset info">
                     </av>
                     <av name=note descr="No Description">
                                          <val value="the non-square pattern of offsets in the jitter below avoids re-inforcement of poor row/column\n\nIf you want to use a different size of offset, move the highlight to the offset\nitem below and edit them.">
                     </av>
                  </no>
                  <ic name=new type=ic subtype=offset>
                     <av name=Offset8 descr="No Description">
                                          <val value="0.0">
                                          <val value="0.0">
                     </av>
                     <av name=Offset7 descr="No Description">
                                          <val value="22.0">
                                          <val value="-22.0">
                     </av>
                     <av name=Offset5 descr="No Description">
                                          <val value="-22.0">
                                          <val value="-20.0">
                     </av>
                     <av name=Offset3 descr="No Description">
                                          <val value="-20.0">
                                          <val value="22.0">
                     </av>
                     <av name=title descr="No Description">
                                          <val value="jitter_5_20as">
                     </av>
                     <av name=.gui.selectedOffsetPos descr="No Description">
                                          <val value="Offset8">
                     </av>
                     <av name=Offset1 descr="No Description">
                                          <val value="21.0">
                                          <val value="20.0">
                     </av>
                     <av name=offsetPositions descr="No Description">
                                          <val value="Offset8">
                                          <val value="Offset1">
                                          <val value="Offset3">
                                          <val value="Offset5">
                                          <val value="Offset7">
                     </av>
                     <av name=.gui.collapsed descr="No Description">
                                          <val value="false">
                     </av>
                     <ic name=new type=ic subtype=observe>
                        <av name=repeatCount descr="No Description">
                                                <val value="1">
                        </av>
                     </ic>
                  </ic>
               </ic>
               <no name=new type=no subtype=none>
                  <av name=title descr="No Description">
                                    <val value="last offset">
                  </av>
                  <av name=note descr="No Description">
                                    <val value="This final offset ensures the telescope sets back to 00 after all the observations are finished.">
                  </av>
               </no>
               <ic name=new type=ic subtype=offset>
                  <av name=offsetPositions descr="No Description">
                                    <val value="Offset0">
                  </av>
                  <av name=Offset0 descr="No Description">
                                    <val value="0.0">
                                    <val value="0.0">
                  </av>
                  <av name=.gui.selectedOffsetPos descr="No Description">
                                    <val value="Offset0">
                  </av>
               </ic>
            </if>
         </ob>
         <ob name=new type=ob subtype=none>
            <av name=title descr="No Description">
                        <val value="Quadrant Jitter">
            </av>
            <av name=standard descr="No Description">
                        <val value="false">
            </av>
            <av name=chainedToPrev descr="No Description">
                        <val value="false">
            </av>
            <av name=chainedToNext descr="No Description">
                        <val value="false">
            </av>
            <av name=.gui.collapsed descr="No Description">
                        <val value="true">
            </av>
            <no name=new type=no subtype=none>
               <av name=title descr="No Description">
                              <val value="target information">
               </av>
               <av name=note descr="No Description">
                              <val value="enter your target co-ordinates into the target list below.  Note that although this is called a \"target list\" it really only allows you to specify ONE astronomical target\n\nWhen you have done so you can use the \"position editor\" to  select a guide star, to fine tune the co-ordinate based on a sky survey picture, examine offset locations on the field etc.\n\nFor a step by step guide to how to use the position editor use \"help\".">
               </av>
            </no>
            <oc name=new type=oc subtype=targetList>
               <av name=Base descr="No Description">
                              <val value="Base">
                              <val value="enter your object name">
                              <val value="0:00:00">
                              <val value="0:00:00">
                              <val value="FK5 (J2000)">
               </av>
               <av name=.unique descr="No Description">
                              <val value="true">
               </av>
               <av name=.gui.selectedTelescopePos descr="No Description">
                              <val value="Base">
               </av>
            </oc>
            <no name=new type=no subtype=none>
               <av name=title descr="No Description">
                              <val value="how to set the exposure time and filter">
               </av>
               <av name=note descr="No Description">
                              <val value="The UFTI component below uses the K filter.  To change it move the highlight to the \"ufti component\" below and select the filter you want to use.\n\nThe UFTI component below uses a 40 second expsoure time.   To change it move the highlight to \"ufti\" below and change the exposure time - either enter the value you want to use, or enter the magnitude range of your target and the recommended exposure time will be automatically entered.\n\n">
               </av>
            </no>
            <oc name=new type=oc subtype=inst.UFTI>
               <av name=instPort descr="No Description">
                              <val value="East">
               </av>
               <av name=sourceMag descr="No Description">
                              <val value="BL">
               </av>
               <av name=.unique descr="No Description">
                              <val value="true">
               </av>
               <av name=filter descr="No Description">
                              <val value="K98">
               </av>
               <av name=posAngle descr="No Description">
                              <val value="0.0">
               </av>
               <av name=coadds descr="No Description">
                              <val value="1">
               </av>
               <av name=instAper descr="No Description">
                              <val value="0.998">
                              <val value="-0.62">
                              <val value="0.0">
                              <val value="2.150">
               </av>
               <av name=exposureTime descr="No Description">
                              <val value="60.0">
               </av>
               <av name=readoutArea descr="No Description">
                              <val value="1024x1024">
               </av>
               <av name=title descr="No Description">
                              <val value="- set configuration">
               </av>
               <av name=acqMode descr="No Description">
                              <val value="Normal+NDSTARE">
               </av>
               <av name=.version descr="No Description">
                              <val value="1.0">
               </av>
            </oc>
            <no name=new type=no subtype=none>
               <av name=title descr="No Description">
                              <val value="DR Recipe Selection">
               </av>
               <av name=note descr="No Description">
                              <val value="The data reduction recipe specified below uses the most basic data reduction recipe for speed.  (this is a good idea if your exposure time short).\n\nIf you need to use a more complete recipe then select QUADRANT_JITTER or ....\n">
               </av>
            </no>
            <oc name=new type=oc subtype=DRRecipe>
               <av name=ObjectRecipe descr="No Description">
                              <val value="QUICK_LOOK">
               </av>
               <av name=DRRecipe descr="No Description">
                              <val value="QUADRANT_JITTER_BASIC">
               </av>
               <av name=DarkRecipe descr="No Description">
                              <val value="REDUCE_DARK">
               </av>
               <av name=SkyInGroup descr="No Description">
                              <val value="true">
               </av>
               <av name=.unique descr="No Description">
                              <val value="true">
               </av>
               <av name=ObjectInGroup descr="No Description">
                              <val value="true">
               </av>
               <av name=FlatRecipe descr="No Description">
                              <val value="REDUCE_FLAT">
               </av>
               <av name=DarkInGroup descr="No Description">
                              <val value="false">
               </av>
               <av name=SkyRecipe descr="No Description">
                              <val value="REDUCE_SKY">
               </av>
               <av name=ArcRecipe descr="No Description">
                              <val value="REDUCE_ARC">
               </av>
               <av name=BiasInGroup descr="No Description">
                              <val value="false">
               </av>
               <av name=title descr="No Description">
                              <val value="QUADRANT_JITTER_BASIC">
               </av>
               <av name=FlatInGroup descr="No Description">
                              <val value="false">
               </av>
               <av name=BiasRecipe descr="No Description">
                              <val value="REDUCE_BIAS">
               </av>
               <av name=ArcInGroup descr="No Description">
                              <val value="false">
               </av>
            </oc>
            <if name=new type=if subtype=none>
               <av name=.gui.collapsed descr="No Description">
                              <val value="false">
               </av>
               <no name=new type=no subtype=none>
                  <av name=title descr="No Description">
                                    <val value="change dark exp time">
                  </av>
                  <av name=note descr="No Description">
                                    <val value="If you changed the exposure time in the UFTI component then change the\nexposure time here to match it.\n\n\nNB :\nThe data reduction recipe will not work unless you have a dark with the right exposure time\n\nUse the reset to default button to set the exposure time to match your object exposure time above">
                  </av>
               </no>
               <ic name=new type=ic subtype=darkObs>
                  <av name=coadds descr="No Description">
                                    <val value="1">
                  </av>
                  <av name=repeatCount descr="No Description">
                                    <val value="1">
                  </av>
                  <av name=exposureTime descr="No Description">
                                    <val value="60.0">
                  </av>
               </ic>
               <no name=new type=no subtype=none>
                  <av name=title descr="No Description">
                                    <val value="how to do more than one quadrant jitters">
                  </av>
                  <av name=note descr="No Description">
                                    <val value="To repeat the quadrant pattern more than once on your target , use the repeat item below to specify how many times you want to repeat it.\n\n">
                  </av>
               </no>
               <ic name=new type=ic subtype=repeat>
                  <av name=.gui.collapsed descr="No Description">
                                    <val value="false">
                  </av>
                  <no name=new type=no subtype=none>
                     <av name=title descr="No Description">
                                          <val value="offset info">
                     </av>
                     <av name=note descr="No Description">
                                          <val value="\n\nIf you want to use a different size of offset, move the highlight to the offset\nitem below and edit them.\n\nChanging the total number of offsets\nwill break the data reduction recipe \n\nQuadrant jitters move the source into each quadrant of the array.  The flat field is generated for each quadrant by using the data from the frames for which the source was not in that\nquadrant.  This works well for small extended sources.">
                     </av>
                  </no>
                  <ic name=new type=ic subtype=offset>
                     <av name=.gui.selectedOffsetPos descr="No Description">
                                          <val value="Offset1">
                     </av>
                     <av name=Offset1 descr="No Description">
                                          <val value="20.0">
                                          <val value="20.0">
                     </av>
                     <av name=Offset0 descr="No Description">
                                          <val value="21.0">
                                          <val value="-20.0">
                     </av>
                     <av name=offsetPositions descr="No Description">
                                          <val value="Offset1">
                                          <val value="Offset0">
                                          <val value="Offset5">
                                          <val value="Offset7">
                     </av>
                     <av name=title descr="No Description">
                                          <val value="quadrant jitter">
                     </av>
                     <av name=.gui.collapsed descr="No Description">
                                          <val value="false">
                     </av>
                     <av name=Offset7 descr="No Description">
                                          <val value="-21.0">
                                          <val value="21.0">
                     </av>
                     <av name=Offset5 descr="No Description">
                                          <val value="-20.0">
                                          <val value="-21.0">
                     </av>
                     <ic name=new type=ic subtype=observe>
                        <av name=repeatCount descr="No Description">
                                                <val value="1">
                        </av>
                     </ic>
                  </ic>
               </ic>
               <no name=new type=no subtype=none>
                  <av name=title descr="No Description">
                                    <val value="final offset">
                  </av>
                  <av name=note descr="No Description">
                                    <val value="For convenience this final offset ensures the telescope moves back to 00 after all the observations have completed,">
                  </av>
               </no>
               <ic name=new type=ic subtype=offset>
                  <av name=offsetPositions descr="No Description">
                                    <val value="Offset0">
                  </av>
                  <av name=Offset0 descr="No Description">
                                    <val value="0.0">
                                    <val value="0.0">
                  </av>
                  <av name=.gui.selectedOffsetPos descr="No Description">
                                    <val value="Offset0">
                  </av>
               </ic>
            </if>
         </ob>
         <ob name=new type=ob subtype=none>
            <av name=title descr="No Description">
                        <val value="sky and Jitter5">
            </av>
            <av name=standard descr="No Description">
                        <val value="false">
            </av>
            <av name=chainedToPrev descr="No Description">
                        <val value="false">
            </av>
            <av name=chainedToNext descr="No Description">
                        <val value="false">
            </av>
            <av name=.gui.collapsed descr="No Description">
                        <val value="true">
            </av>
            <no name=new type=no subtype=none>
               <av name=title descr="No Description">
                              <val value="target information">
               </av>
               <av name=note descr="No Description">
                              <val value="enter your target co-ordinates into the target list below.  Note that although this is called a \"target list\" it really only allows you to specify ONE astronomical target\n\nWhen you have done so you can use the \"position editor\" to  select a guide star, to fine tune the co-ordinate based on a sky survey picture, examine offset locations on the field etc.\n\nFor a step by step guide to how to use the position editor use \"help\".">
               </av>
            </no>
            <oc name=new type=oc subtype=targetList>
               <av name=Base descr="No Description">
                              <val value="Base">
                              <val value="enter your object name">
                              <val value="0:00:00">
                              <val value="0:00:00">
                              <val value="FK5 (J2000)">
               </av>
               <av name=.unique descr="No Description">
                              <val value="true">
               </av>
               <av name=.gui.selectedTelescopePos descr="No Description">
                              <val value="Base">
               </av>
            </oc>
            <no name=new type=no subtype=none>
               <av name=title descr="No Description">
                              <val value="how to set the exposure time and filter">
               </av>
               <av name=note descr="No Description">
                              <val value="The UFTI component below uses the K filter.  To change it move the highlight to the \"ufti component\" below and select the filter you want to use.\n\nThe UFTI component below uses a 40 second expsoure time.   To change it move the highlight to \"ufti\" below and change the exposure time - either enter the value you want to use, or enter the magnitude range of your target and the recommended exposure time will be automatically entered.\n\n">
               </av>
            </no>
            <oc name=new type=oc subtype=inst.UFTI>
               <av name=instPort descr="No Description">
                              <val value="East">
               </av>
               <av name=sourceMag descr="No Description">
                              <val value="BL">
               </av>
               <av name=.unique descr="No Description">
                              <val value="true">
               </av>
               <av name=filter descr="No Description">
                              <val value="K98">
               </av>
               <av name=posAngle descr="No Description">
                              <val value="0.0">
               </av>
               <av name=coadds descr="No Description">
                              <val value="1">
               </av>
               <av name=instAper descr="No Description">
                              <val value="0.998">
                              <val value="-0.62">
                              <val value="0.0">
                              <val value="2.150">
               </av>
               <av name=exposureTime descr="No Description">
                              <val value="60.0">
               </av>
               <av name=readoutArea descr="No Description">
                              <val value="1024x1024">
               </av>
               <av name=title descr="No Description">
                              <val value="- set configuration">
               </av>
               <av name=acqMode descr="No Description">
                              <val value="Normal+NDSTARE">
               </av>
               <av name=.version descr="No Description">
                              <val value="1.0">
               </av>
            </oc>
            <no name=new type=no subtype=none>
               <av name=title descr="No Description">
                              <val value="DR Recipe Selection">
               </av>
               <av name=note descr="No Description">
                              <val value="The data reduction recipe used assumes that a flat field has already been created, It will not work unless you have done this.  Use the observation \"make sky flats\" to do this.">
               </av>
            </no>
            <oc name=new type=oc subtype=DRRecipe>
               <av name=ObjectRecipe descr="No Description">
                              <val value="SKY_AND_JITTER5">
               </av>
               <av name=DRRecipe descr="No Description">
                              <val value="SKY_AND_JITTER5_APHOT">
               </av>
               <av name=DarkRecipe descr="No Description">
                              <val value="REDUCE_DARK">
               </av>
               <av name=SkyInGroup descr="No Description">
                              <val value="true">
               </av>
               <av name=.unique descr="No Description">
                              <val value="true">
               </av>
               <av name=ObjectInGroup descr="No Description">
                              <val value="true">
               </av>
               <av name=FlatRecipe descr="No Description">
                              <val value="REDUCE_FLAT">
               </av>
               <av name=DarkInGroup descr="No Description">
                              <val value="false">
               </av>
               <av name=SkyRecipe descr="No Description">
                              <val value="REDUCE_SKY">
               </av>
               <av name=ArcRecipe descr="No Description">
                              <val value="REDUCE_ARC">
               </av>
               <av name=BiasInGroup descr="No Description">
                              <val value="false">
               </av>
               <av name=title descr="No Description">
                              <val value="SKY_AND_JITTER5">
               </av>
               <av name=FlatInGroup descr="No Description">
                              <val value="false">
               </av>
               <av name=BiasRecipe descr="No Description">
                              <val value="REDUCE_BIAS">
               </av>
               <av name=ArcInGroup descr="No Description">
                              <val value="false">
               </av>
            </oc>
            <if name=new type=if subtype=none>
               <av name=.gui.collapsed descr="No Description">
                              <val value="false">
               </av>
               <no name=new type=no subtype=none>
                  <av name=title descr="No Description">
                                    <val value="change dark exp time here">
                  </av>
                  <av name=note descr="No Description">
                                    <val value="IF you changed the UFTI exposurte time in the componet above then change the  dark exp time below to match it\n\nNB :\nThe data reduction recipe will not work unless you have a dark with the right exposure time\n\nuse the reset todefault button to ensure the dark expsure time matches that set for your object observation in the component above.">
                  </av>
               </no>
               <ic name=new type=ic subtype=darkObs>
                  <av name=coadds descr="No Description">
                                    <val value="1">
                  </av>
                  <av name=repeatCount descr="No Description">
                                    <val value="1">
                  </av>
                  <av name=exposureTime descr="No Description">
                                    <val value="60.0">
                  </av>
               </ic>
               <no name=new type=no subtype=none>
                  <av name=title descr="No Description">
                                    <val value="how to do more than one">
                  </av>
                  <av name=note descr="No Description">
                                    <val value="To repeat the pattern more than once on your target , use the repeat item below to specify how many times you want to repeat it.\n\n">
                  </av>
               </no>
               <ic name=new type=ic subtype=repeat>
                  <av name=.gui.collapsed descr="No Description">
                                    <val value="false">
                  </av>
                  <no name=new type=no subtype=none>
                     <av name=title descr="No Description">
                                          <val value="offset info">
                     </av>
                     <av name=note descr="No Description">
                                          <val value="The first offset is to \"sky\"/\"off the array\"\n\nIf you want to use a different size of offset, move the highlight to the offset\nitem below and edit them.\n\nChanging the total number of offsets\nwill break the data reduction recipe \n\nRemoving the offset to sky will break the recipe">
                     </av>
                  </no>
                  <ic name=new type=ic subtype=offset>
                     <av name=.gui.selectedOffsetPos descr="No Description">
                                          <val value="Offset0">
                     </av>
                     <av name=Offset0 descr="No Description">
                                          <val value="60.0">
                                          <val value="0.0">
                     </av>
                     <av name=offsetPositions descr="No Description">
                                          <val value="Offset0">
                     </av>
                     <av name=title descr="No Description">
                                          <val value="sky first">
                     </av>
                     <ic name=new type=ic subtype=observe>
                        <av name=repeatCount descr="No Description">
                                                <val value="1">
                        </av>
                     </ic>
                  </ic>
                  <ic name=new type=ic subtype=offset>
                     <av name=Offset8 descr="No Description">
                                          <val value="0.0">
                                          <val value="0.0">
                     </av>
                     <av name=Offset7 descr="No Description">
                                          <val value="11.0">
                                          <val value="-11.0">
                     </av>
                     <av name=Offset5 descr="No Description">
                                          <val value="-11.0">
                                          <val value="-10.0">
                     </av>
                     <av name=Offset3 descr="No Description">
                                          <val value="-10.0">
                                          <val value="11.0">
                     </av>
                     <av name=title descr="No Description">
                                          <val value="then jitter_5">
                     </av>
                     <av name=.gui.selectedOffsetPos descr="No Description">
                                          <val value="Offset8">
                     </av>
                     <av name=Offset1 descr="No Description">
                                          <val value="10.0">
                                          <val value="10.0">
                     </av>
                     <av name=offsetPositions descr="No Description">
                                          <val value="Offset8">
                                          <val value="Offset1">
                                          <val value="Offset3">
                                          <val value="Offset5">
                                          <val value="Offset7">
                     </av>
                     <av name=.gui.collapsed descr="No Description">
                                          <val value="false">
                     </av>
                     <ic name=new type=ic subtype=observe>
                        <av name=repeatCount descr="No Description">
                                                <val value="1">
                        </av>
                     </ic>
                  </ic>
               </ic>
               <no name=new type=no subtype=none>
                  <av name=title descr="No Description">
                                    <val value="last offset">
                  </av>
                  <av name=note descr="No Description">
                                    <val value="This last offset takes the telescope back to the 00 position for convenience.">
                  </av>
               </no>
               <ic name=new type=ic subtype=offset>
                  <av name=.gui.selectedOffsetPos descr="No Description">
                                    <val value="Offset0">
                  </av>
                  <av name=Offset0 descr="No Description">
                                    <val value="0.0">
                                    <val value="0.0">
                  </av>
                  <av name=offsetPositions descr="No Description">
                                    <val value="Offset0">
                  </av>
                  <av name=title descr="No Description">
                                    <val value="back to 00">
                  </av>
               </ic>
            </if>
         </ob>
         <ob name=new type=ob subtype=none>
            <av name=title descr="No Description">
                        <val value="extended_5x5">
            </av>
            <av name=standard descr="No Description">
                        <val value="false">
            </av>
            <av name=chainedToPrev descr="No Description">
                        <val value="false">
            </av>
            <av name=chainedToNext descr="No Description">
                        <val value="false">
            </av>
            <av name=.gui.collapsed descr="No Description">
                        <val value="true">
            </av>
            <no name=new type=no subtype=none>
               <av name=title descr="No Description">
                              <val value="USEAGE">
               </av>
               <av name=note descr="No Description">
                              <val value="Exec for making a map of a large source .  Images are taken in strips with semi-random ordering of postions in the strip and of the strip to minimise systematic errors due to sky fluctuations. Sky frames are taken alternately with those of the object.\n \nThe offset to \"sky\" is 180 arcsec.  For this offset you can guide at all positions if you are guding on target, or a very nearby star.  Otherwise some\npositions will be \"un guided\"\n\nIt starts with sky then the \"middle\" frame.">
               </av>
            </no>
            <no name=new type=no subtype=none>
               <av name=title descr="No Description">
                              <val value="target information">
               </av>
               <av name=note descr="No Description">
                              <val value="enter your target co-ordinates into the target list below.  Note that although this is called a \"target list\" it really only allows you to specify ONE astronomical target\n\nWhen you have done so you can use the \"position editor\" to  select a guide star, to fine tune the co-ordinate based on a sky survey picture, examine offset locations on the field etc.\n\nFor a step by step guide to how to use the position editor use \"help\".">
               </av>
            </no>
            <oc name=new type=oc subtype=targetList>
               <av name=Base descr="No Description">
                              <val value="Base">
                              <val value="enter your object name">
                              <val value="0:00:00">
                              <val value="0:00:00">
                              <val value="FK5 (J2000)">
               </av>
               <av name=.unique descr="No Description">
                              <val value="true">
               </av>
               <av name=.gui.selectedTelescopePos descr="No Description">
                              <val value="Base">
               </av>
            </oc>
            <no name=new type=no subtype=none>
               <av name=title descr="No Description">
                              <val value="how to set the exposure time and filter">
               </av>
               <av name=note descr="No Description">
                              <val value="The UFTI component below uses the K filter.  To change it move the highlight to the \"ufti component\" below and select the filter you want to use.\n\nThe UFTI component below uses a 60 second expsoure time.   To change it move the highlight to \"ufti\" below and change the exposure time - either enter the value you want to use, or enter the magnitude range of your target and the recommended exposure time will be automatically entered.\n\n">
               </av>
            </no>
            <oc name=new type=oc subtype=inst.UFTI>
               <av name=instPort descr="No Description">
                              <val value="East">
               </av>
               <av name=sourceMag descr="No Description">
                              <val value="BL">
               </av>
               <av name=.unique descr="No Description">
                              <val value="true">
               </av>
               <av name=filter descr="No Description">
                              <val value="K98">
               </av>
               <av name=posAngle descr="No Description">
                              <val value="0.0">
               </av>
               <av name=coadds descr="No Description">
                              <val value="1">
               </av>
               <av name=instAper descr="No Description">
                              <val value="0.998">
                              <val value="-0.62">
                              <val value="0.0">
                              <val value="2.150">
               </av>
               <av name=exposureTime descr="No Description">
                              <val value="60.0">
               </av>
               <av name=readoutArea descr="No Description">
                              <val value="1024x1024">
               </av>
               <av name=title descr="No Description">
                              <val value="- set configuration">
               </av>
               <av name=acqMode descr="No Description">
                              <val value="Normal+NDSTARE">
               </av>
               <av name=.version descr="No Description">
                              <val value="1.0">
               </av>
            </oc>
            <no name=new type=no subtype=none>
               <av name=title descr="No Description">
                              <val value="DR Recipe Selection">
               </av>
               <av name=note descr="No Description">
                              <val value="The data reduction recipe assumes the\norder in which the images are taken\n- if you change this it wont work.">
               </av>
            </no>
            <oc name=new type=oc subtype=DRRecipe>
               <av name=ObjectRecipe descr="No Description">
                              <val value="EXTENDED_5x5">
               </av>
               <av name=DRRecipe descr="No Description">
                              <val value="EXTENDED_5x5">
               </av>
               <av name=DarkRecipe descr="No Description">
                              <val value="REDUCE_DARK">
               </av>
               <av name=SkyInGroup descr="No Description">
                              <val value="true">
               </av>
               <av name=.unique descr="No Description">
                              <val value="true">
               </av>
               <av name=ObjectInGroup descr="No Description">
                              <val value="true">
               </av>
               <av name=FlatRecipe descr="No Description">
                              <val value="REDUCE_FLAT">
               </av>
               <av name=DarkInGroup descr="No Description">
                              <val value="false">
               </av>
               <av name=SkyRecipe descr="No Description">
                              <val value="REDUCE_SKY">
               </av>
               <av name=ArcRecipe descr="No Description">
                              <val value="REDUCE_ARC">
               </av>
               <av name=BiasInGroup descr="No Description">
                              <val value="false">
               </av>
               <av name=title descr="No Description">
                              <val value="EXTENDED_5x5">
               </av>
               <av name=FlatInGroup descr="No Description">
                              <val value="false">
               </av>
               <av name=BiasRecipe descr="No Description">
                              <val value="REDUCE_BIAS">
               </av>
               <av name=ArcInGroup descr="No Description">
                              <val value="false">
               </av>
            </oc>
            <if name=new type=if subtype=none>
               <av name=.gui.collapsed descr="No Description">
                              <val value="false">
               </av>
               <no name=new type=no subtype=none>
                  <av name=title descr="No Description">
                                    <val value="change dark exp time here">
                  </av>
                  <av name=note descr="No Description">
                                    <val value="If you changed the exposure time for the UFIT configuration, then change it for the dark here too.\n\nthe data reduction recipe will not work unless you have a dark with the right exposure time\n\nuse the reset to default button to set the exposure time for the dark equal to that for the \nsource that you have set above.">
                  </av>
               </no>
               <ic name=new type=ic subtype=darkObs>
                  <av name=coadds descr="No Description">
                                    <val value="1">
                  </av>
                  <av name=repeatCount descr="No Description">
                                    <val value="1">
                  </av>
                  <av name=exposureTime descr="No Description">
                                    <val value="60.0">
                  </av>
               </ic>
               <ic name=new type=ic subtype=offset>
                  <av name=title descr="No Description">
                                    <val value="extended_5x5">
                  </av>
                  <av name=Offset50 descr="No Description">
                                    <val value="185.0">
                                    <val value="-5.0">
                  </av>
                  <av name=.gui.collapsed descr="No Description">
                                    <val value="false">
                  </av>
                  <av name=Offset49 descr="No Description">
                                    <val value="90.0">
                                    <val value="90.0">
                  </av>
                  <av name=Offset48 descr="No Description">
                                    <val value="185.0">
                                    <val value="5.0">
                  </av>
                  <av name=Offset47 descr="No Description">
                                    <val value="-45.0">
                                    <val value="90.0">
                  </av>
                  <av name=Offset46 descr="No Description">
                                    <val value="185.0">
                                    <val value="0.0">
                  </av>
                  <av name=Offset45 descr="No Description">
                                    <val value="45.0">
                                    <val value="90.0">
                  </av>
                  <av name=Offset44 descr="No Description">
                                    <val value="-185.0">
                                    <val value="-5.0">
                  </av>
                  <av name=Offset43 descr="No Description">
                                    <val value="-90.0">
                                    <val value="90.0">
                  </av>
                  <av name=Offset42 descr="No Description">
                                    <val value="-185.0">
                                    <val value="5.0">
                  </av>
                  <av name=Offset41 descr="No Description">
                                    <val value="0.0">
                                    <val value="90.0">
                  </av>
                  <av name=Offset40 descr="No Description">
                                    <val value="-185.0">
                                    <val value="0.0">
                  </av>
                  <av name=Offset9 descr="No Description">
                                    <val value="90.0">
                                    <val value="0.0">
                  </av>
                  <av name=Offset8 descr="No Description">
                                    <val value="180.0">
                                    <val value="0.0">
                  </av>
                  <av name=.gui.selectedOffsetPos descr="No Description">
                                    <val value="Offset1">
                  </av>
                  <av name=Offset7 descr="No Description">
                                    <val value="-90.0">
                                    <val value="0.0">
                  </av>
                  <av name=Offset6 descr="No Description">
                                    <val value="-45.0">
                                    <val value="0.0">
                  </av>
                  <av name=Offset5 descr="No Description">
                                    <val value="-185.0">
                                    <val value="180.0">
                  </av>
                  <av name=Offset4 descr="No Description">
                                    <val value="-180.0">
                                    <val value="185.0">
                  </av>
                  <av name=Offset3 descr="No Description">
                                    <val value="0.0">
                                    <val value="0.0">
                  </av>
                  <av name=Offset2 descr="No Description">
                                    <val value="45.0">
                                    <val value="0.0">
                  </av>
                  <av name=Offset1 descr="No Description">
                                    <val value="-180.0">
                                    <val value="180.0">
                  </av>
                  <av name=Offset0 descr="No Description">
                                    <val value="-185.0">
                                    <val value="185.0">
                  </av>
                  <av name=Offset39 descr="No Description">
                                    <val value="90.0">
                                    <val value="-45.0">
                  </av>
                  <av name=Offset38 descr="No Description">
                                    <val value="-180.0">
                                    <val value="-5.0">
                  </av>
                  <av name=Offset37 descr="No Description">
                                    <val value="-45.0">
                                    <val value="-45.0">
                  </av>
                  <av name=Offset36 descr="No Description">
                                    <val value="-180.0">
                                    <val value="5.0">
                  </av>
                  <av name=Offset35 descr="No Description">
                                    <val value="45.0">
                                    <val value="-45.0">
                  </av>
                  <av name=Offset34 descr="No Description">
                                    <val value="-180.0">
                                    <val value="0.0">
                  </av>
                  <av name=Offset33 descr="No Description">
                                    <val value="-90.0">
                                    <val value="-45.0">
                  </av>
                  <av name=Offset32 descr="No Description">
                                    <val value="-180.0">
                                    <val value="175.0">
                  </av>
                  <av name=Offset31 descr="No Description">
                                    <val value="0.0">
                                    <val value="-45.0">
                  </av>
                  <av name=Offset30 descr="No Description">
                                    <val value="180.0">
                                    <val value="175.0">
                  </av>
                  <av name=Offset29 descr="No Description">
                                    <val value="-90.0">
                                    <val value="45.0">
                  </av>
                  <av name=Offset28 descr="No Description">
                                    <val value="180.0">
                                    <val value="185.0">
                  </av>
                  <av name=Offset27 descr="No Description">
                                    <val value="45.0">
                                    <val value="45.0">
                  </av>
                  <av name=Offset26 descr="No Description">
                                    <val value="185.0">
                                    <val value="185.0">
                  </av>
                  <av name=Offset25 descr="No Description">
                                    <val value="-45.0">
                                    <val value="45.0">
                  </av>
                  <av name=Offset24 descr="No Description">
                                    <val value="185.0">
                                    <val value="180.0">
                  </av>
                  <av name=Offset23 descr="No Description">
                                    <val value="90.0">
                                    <val value="45.0">
                  </av>
                  <av name=Offset22 descr="No Description">
                                    <val value="180.0">
                                    <val value="180.0">
                  </av>
                  <av name=Offset21 descr="No Description">
                                    <val value="0.0">
                                    <val value="45.0">
                  </av>
                  <av name=Offset20 descr="No Description">
                                    <val value="180.0">
                                    <val value="-5.0">
                  </av>
                  <av name=offsetPositions descr="No Description">
                                    <val value="Offset1">
                                    <val value="Offset3">
                                    <val value="Offset5">
                                    <val value="Offset7">
                                    <val value="Offset0">
                                    <val value="Offset2">
                                    <val value="Offset4">
                                    <val value="Offset6">
                                    <val value="Offset8">
                                    <val value="Offset9">
                                    <val value="Offset10">
                                    <val value="Offset11">
                                    <val value="Offset12">
                                    <val value="Offset13">
                                    <val value="Offset14">
                                    <val value="Offset15">
                                    <val value="Offset16">
                                    <val value="Offset17">
                                    <val value="Offset18">
                                    <val value="Offset19">
                                    <val value="Offset20">
                                    <val value="Offset21">
                                    <val value="Offset22">
                                    <val value="Offset23">
                                    <val value="Offset24">
                                    <val value="Offset25">
                                    <val value="Offset26">
                                    <val value="Offset27">
                                    <val value="Offset28">
                                    <val value="Offset29">
                                    <val value="Offset30">
                                    <val value="Offset31">
                                    <val value="Offset32">
                                    <val value="Offset33">
                                    <val value="Offset34">
                                    <val value="Offset35">
                                    <val value="Offset36">
                                    <val value="Offset37">
                                    <val value="Offset38">
                                    <val value="Offset39">
                                    <val value="Offset40">
                                    <val value="Offset41">
                                    <val value="Offset42">
                                    <val value="Offset43">
                                    <val value="Offset44">
                                    <val value="Offset45">
                                    <val value="Offset46">
                                    <val value="Offset47">
                                    <val value="Offset48">
                                    <val value="Offset49">
                                    <val value="Offset50">
                  </av>
                  <av name=Offset19 descr="No Description">
                                    <val value="-90.0">
                                    <val value="-90.0">
                  </av>
                  <av name=Offset18 descr="No Description">
                                    <val value="185.0">
                                    <val value="-180.0">
                  </av>
                  <av name=Offset17 descr="No Description">
                                    <val value="45.0">
                                    <val value="-90.0">
                  </av>
                  <av name=Offset16 descr="No Description">
                                    <val value="185.0">
                                    <val value="-185.0">
                  </av>
                  <av name=Offset15 descr="No Description">
                                    <val value="-45.0">
                                    <val value="-90.0">
                  </av>
                  <av name=Offset14 descr="No Description">
                                    <val value="180.0">
                                    <val value="-185.0">
                  </av>
                  <av name=Offset13 descr="No Description">
                                    <val value="90.0">
                                    <val value="-90.0">
                  </av>
                  <av name=Offset12 descr="No Description">
                                    <val value="180.0">
                                    <val value="-180.0">
                  </av>
                  <av name=Offset11 descr="No Description">
                                    <val value="0.0">
                                    <val value="-90.0">
                  </av>
                  <av name=Offset10 descr="No Description">
                                    <val value="180.0">
                                    <val value="5.0">
                  </av>
                  <ic name=new type=ic subtype=observe>
                     <av name=repeatCount descr="No Description">
                                          <val value="1">
                     </av>
                  </ic>
               </ic>
               <no name=new type=no subtype=none>
                  <av name=title descr="No Description">
                                    <val value="back to 00">
                  </av>
                  <av name=note descr="No Description">
                                    <val value="This last offset resets the telescope to 00 after all data tacking completed">
                  </av>
               </no>
               <ic name=new type=ic subtype=offset>
                  <av name=offsetPositions descr="No Description">
                                    <val value="Offset0">
                  </av>
                  <av name=Offset0 descr="No Description">
                                    <val value="0.0">
                                    <val value="0.0">
                  </av>
                  <av name=.gui.selectedOffsetPos descr="No Description">
                                    <val value="Offset0">
                  </av>
               </ic>
            </if>
         </ob>
         <ob name=new type=ob subtype=none>
            <av name=title descr="No Description">
                        <val value="make_skyflats">
            </av>
            <av name=standard descr="No Description">
                        <val value="false">
            </av>
            <av name=chainedToPrev descr="No Description">
                        <val value="false">
            </av>
            <av name=chainedToNext descr="No Description">
                        <val value="false">
            </av>
            <av name=.gui.collapsed descr="No Description">
                        <val value="true">
            </av>
            <no name=new type=no subtype=none>
               <av name=title descr="No Description">
                              <val value="useage">
               </av>
               <av name=note descr="No Description">
                              <val value="Use this observation to make some\nobservations of blank sky which can be used to generate flat fields if you\nare observing sources where the exposure time is too short to generate a flat field from the object jitter pattern,\n\nThe reduction recipe makes a flat that other reduction recipes can use">
               </av>
            </no>
            <no name=new type=no subtype=none>
               <av name=title descr="No Description">
                              <val value="target information">
               </av>
               <av name=note descr="No Description">
                              <val value="Select a piece of blank sky near your target\n\nFor a step by step guide to how to use the position editor use \"help\".">
               </av>
            </no>
            <oc name=new type=oc subtype=targetList>
               <av name=Base descr="No Description">
                              <val value="Base">
                              <val value="enter your object name">
                              <val value="0:00:00">
                              <val value="0:00:00">
                              <val value="FK5 (J2000)">
               </av>
               <av name=.unique descr="No Description">
                              <val value="true">
               </av>
               <av name=.gui.selectedTelescopePos descr="No Description">
                              <val value="Base">
               </av>
            </oc>
            <no name=new type=no subtype=none>
               <av name=title descr="No Description">
                              <val value="how to set the exposure time and filter">
               </av>
               <av name=note descr="No Description">
                              <val value="The UFTI component below uses the J filter.  To change it move the highlight to the \"ufti component\" below and select the filter you want to use.\n\nThe UFTI component below uses a 40 second expsoure time.   To change it move the highlight to \"ufti\" below and change the exposure time - either enter the value you want to use, or enter the magnitude range of your target and the recommended exposure time will be automatically entered.\n\nThe sequence below changes from the J to the H and K filters.">
               </av>
            </no>
            <oc name=new type=oc subtype=inst.UFTI>
               <av name=instPort descr="No Description">
                              <val value="East">
               </av>
               <av name=sourceMag descr="No Description">
                              <val value="BL">
               </av>
               <av name=.unique descr="No Description">
                              <val value="true">
               </av>
               <av name=filter descr="No Description">
                              <val value="J98">
               </av>
               <av name=posAngle descr="No Description">
                              <val value="0.0">
               </av>
               <av name=coadds descr="No Description">
                              <val value="1">
               </av>
               <av name=instAper descr="No Description">
                              <val value="0.998">
                              <val value="-0.62">
                              <val value="0.0">
                              <val value="1.250">
               </av>
               <av name=exposureTime descr="No Description">
                              <val value="40.0">
               </av>
               <av name=readoutArea descr="No Description">
                              <val value="1024x1024">
               </av>
               <av name=title descr="No Description">
                              <val value="- set basic configuration">
               </av>
               <av name=acqMode descr="No Description">
                              <val value="Normal+NDSTARE">
               </av>
               <av name=.version descr="No Description">
                              <val value="1.0">
               </av>
            </oc>
            <no name=new type=no subtype=none>
               <av name=title descr="No Description">
                              <val value="DR Recipe Selection">
               </av>
               <av name=note descr="No Description">
                              <val value="\n\nThis recipe generates a flat field that can be used by other data reduction recipes.">
               </av>
            </no>
            <oc name=new type=oc subtype=DRRecipe>
               <av name=ObjectRecipe descr="No Description">
                              <val value="sky_flat">
               </av>
               <av name=DRRecipe descr="No Description">
                              <val value="SKY_FLAT">
               </av>
               <av name=DarkRecipe descr="No Description">
                              <val value="REDUCE_DARK">
               </av>
               <av name=SkyInGroup descr="No Description">
                              <val value="true">
               </av>
               <av name=.unique descr="No Description">
                              <val value="true">
               </av>
               <av name=ObjectInGroup descr="No Description">
                              <val value="true">
               </av>
               <av name=FlatRecipe descr="No Description">
                              <val value="REDUCE_FLAT">
               </av>
               <av name=DarkInGroup descr="No Description">
                              <val value="false">
               </av>
               <av name=SkyRecipe descr="No Description">
                              <val value="REDUCE_SKY">
               </av>
               <av name=ArcRecipe descr="No Description">
                              <val value="REDUCE_ARC">
               </av>
               <av name=BiasInGroup descr="No Description">
                              <val value="false">
               </av>
               <av name=title descr="No Description">
                              <val value="sky_flat">
               </av>
               <av name=FlatInGroup descr="No Description">
                              <val value="false">
               </av>
               <av name=BiasRecipe descr="No Description">
                              <val value="REDUCE_BIAS">
               </av>
               <av name=ArcInGroup descr="No Description">
                              <val value="false">
               </av>
            </oc>
            <if name=new type=if subtype=none>
               <av name=.gui.collapsed descr="No Description">
                              <val value="false">
               </av>
               <no name=new type=no subtype=none>
                  <av name=title descr="No Description">
                                    <val value="change dark exp time here">
                  </av>
                  <av name=note descr="No Description">
                                    <val value="Change the dark exposure time in the\nitem below if you changed it in the UFTI configuration.\n\nNB :\nThe data reduction recipe will not work unless you have a dark with the right exposure time\n\nUse the reset to default button to ensure the dark\nexposure time matches that in your object">
                  </av>
               </no>
               <ic name=new type=ic subtype=darkObs>
                  <av name=coadds descr="No Description">
                                    <val value="1">
                  </av>
                  <av name=repeatCount descr="No Description">
                                    <val value="1">
                  </av>
                  <av name=exposureTime descr="No Description">
                                    <val value="40.0">
                  </av>
               </ic>
               <ic name=new type=ic subtype=offset>
                  <av name=Offset8 descr="No Description">
                                    <val value="0.0">
                                    <val value="0.0">
                  </av>
                  <av name=Offset7 descr="No Description">
                                    <val value="22.0">
                                    <val value="-22.0">
                  </av>
                  <av name=Offset5 descr="No Description">
                                    <val value="-22.0">
                                    <val value="-20.0">
                  </av>
                  <av name=Offset3 descr="No Description">
                                    <val value="-20.0">
                                    <val value="22.0">
                  </av>
                  <av name=title descr="No Description">
                                    <val value="jitter_5_20as">
                  </av>
                  <av name=.gui.selectedOffsetPos descr="No Description">
                                    <val value="Offset8">
                  </av>
                  <av name=Offset1 descr="No Description">
                                    <val value="21.0">
                                    <val value="20.0">
                  </av>
                  <av name=offsetPositions descr="No Description">
                                    <val value="Offset8">
                                    <val value="Offset1">
                                    <val value="Offset3">
                                    <val value="Offset5">
                                    <val value="Offset7">
                  </av>
                  <av name=.gui.collapsed descr="No Description">
                                    <val value="false">
                  </av>
                  <ic name=new type=ic subtype=observe>
                     <av name=repeatCount descr="No Description">
                                          <val value="1">
                     </av>
                  </ic>
               </ic>
               <ic name=new type=ic subtype=instUFTI>
                  <av name=filterIter descr="No Description">
                                    <val value="H98">
                  </av>
                  <av name=title descr="No Description">
                                    <val value="change filt+exp">
                  </av>
                  <av name=instAperLIter descr="No Description">
                                    <val value="1.635">
                  </av>
                  <av name=exposureTimeIter descr="No Description">
                                    <val value="20">
                  </av>
                  <av name=.gui.collapsed descr="No Description">
                                    <val value="false">
                  </av>
                  <av name=iterConfigList descr="No Description">
                                    <val value="filterIter">
                                    <val value="exposureTimeIter">
                  </av>
                  <ic name=new type=ic subtype=darkObs>
                     <av name=coadds descr="No Description">
                                          <val value="1">
                     </av>
                     <av name=repeatCount descr="No Description">
                                          <val value="1">
                     </av>
                     <av name=exposureTime descr="No Description">
                                          <val value="20.0">
                     </av>
                  </ic>
                  <ic name=new type=ic subtype=offset>
                     <av name=Offset8 descr="No Description">
                                          <val value="0.0">
                                          <val value="0.0">
                     </av>
                     <av name=Offset7 descr="No Description">
                                          <val value="22.0">
                                          <val value="-22.0">
                     </av>
                     <av name=Offset5 descr="No Description">
                                          <val value="-22.0">
                                          <val value="-20.0">
                     </av>
                     <av name=Offset3 descr="No Description">
                                          <val value="-20.0">
                                          <val value="22.0">
                     </av>
                     <av name=title descr="No Description">
                                          <val value="jitter_5_20as">
                     </av>
                     <av name=.gui.selectedOffsetPos descr="No Description">
                                          <val value="Offset8">
                     </av>
                     <av name=Offset1 descr="No Description">
                                          <val value="21.0">
                                          <val value="20.0">
                     </av>
                     <av name=offsetPositions descr="No Description">
                                          <val value="Offset8">
                                          <val value="Offset1">
                                          <val value="Offset3">
                                          <val value="Offset5">
                                          <val value="Offset7">
                     </av>
                     <av name=.gui.collapsed descr="No Description">
                                          <val value="false">
                     </av>
                     <ic name=new type=ic subtype=observe>
                        <av name=repeatCount descr="No Description">
                                                <val value="1">
                        </av>
                     </ic>
                  </ic>
               </ic>
               <ic name=new type=ic subtype=instUFTI>
                  <av name=filterIter descr="No Description">
                                    <val value="K98">
                  </av>
                  <av name=title descr="No Description">
                                    <val value="change filter">
                  </av>
                  <av name=instAperLIter descr="No Description">
                                    <val value="2.150">
                  </av>
                  <av name=iterConfigList descr="No Description">
                                    <val value="filterIter">
                  </av>
                  <ic name=new type=ic subtype=offset>
                     <av name=Offset8 descr="No Description">
                                          <val value="0.0">
                                          <val value="0.0">
                     </av>
                     <av name=Offset7 descr="No Description">
                                          <val value="22.0">
                                          <val value="-22.0">
                     </av>
                     <av name=Offset5 descr="No Description">
                                          <val value="-22.0">
                                          <val value="-20.0">
                     </av>
                     <av name=Offset3 descr="No Description">
                                          <val value="-20.0">
                                          <val value="22.0">
                     </av>
                     <av name=title descr="No Description">
                                          <val value="jitter_5_20as">
                     </av>
                     <av name=.gui.selectedOffsetPos descr="No Description">
                                          <val value="Offset8">
                     </av>
                     <av name=Offset1 descr="No Description">
                                          <val value="21.0">
                                          <val value="20.0">
                     </av>
                     <av name=offsetPositions descr="No Description">
                                          <val value="Offset8">
                                          <val value="Offset1">
                                          <val value="Offset3">
                                          <val value="Offset5">
                                          <val value="Offset7">
                     </av>
                     <av name=.gui.collapsed descr="No Description">
                                          <val value="false">
                     </av>
                     <ic name=new type=ic subtype=observe>
                        <av name=repeatCount descr="No Description">
                                                <val value="1">
                        </av>
                     </ic>
                  </ic>
               </ic>
               <no name=new type=no subtype=none>
                  <av name=title descr="No Description">
                                    <val value="last offset">
                  </av>
                  <av name=note descr="No Description">
                                    <val value="This last offset ensures that the telescope goes\nback to 00 after the last observation ">
                  </av>
               </no>
               <ic name=new type=ic subtype=offset>
                  <av name=offsetPositions descr="No Description">
                                    <val value="Offset0">
                  </av>
                  <av name=Offset0 descr="No Description">
                                    <val value="0.0">
                                    <val value="0.0">
                  </av>
                  <av name=.gui.selectedOffsetPos descr="No Description">
                                    <val value="Offset0">
                  </av>
               </ic>
            </if>
         </ob>
         <ob name=new type=ob subtype=none>
            <av name=title descr="No Description">
                        <val value="make_skyflat_for I filter">
            </av>
            <av name=standard descr="No Description">
                        <val value="false">
            </av>
            <av name=chainedToPrev descr="No Description">
                        <val value="false">
            </av>
            <av name=chainedToNext descr="No Description">
                        <val value="false">
            </av>
            <av name=.gui.collapsed descr="No Description">
                        <val value="true">
            </av>
            <no name=new type=no subtype=none>
               <av name=title descr="No Description">
                              <val value="useage">
               </av>
               <av name=note descr="No Description">
                              <val value="Use this observation to make some\nobservations of blank sky which can be used to generate flat fields if you\nare observing sources where the exposure time is too short to generate a flat field from the object jitter pattern,\n\nThe reduction recipe makes a flat that other reduction recipes can use">
               </av>
            </no>
            <no name=new type=no subtype=none>
               <av name=title descr="No Description">
                              <val value="target information">
               </av>
               <av name=note descr="No Description">
                              <val value="Select a piece of blank sky near your target\n\nput the coordinates into the target list,\n\nFor a step by step guide to how to use the position editor use \"help\".">
               </av>
            </no>
            <oc name=new type=oc subtype=targetList>
               <av name=Base descr="No Description">
                              <val value="Base">
                              <val value="enter your object name">
                              <val value="0:00:00">
                              <val value="0:00:00">
                              <val value="FK5 (J2000)">
               </av>
               <av name=.unique descr="No Description">
                              <val value="true">
               </av>
               <av name=.gui.selectedTelescopePos descr="No Description">
                              <val value="Base">
               </av>
            </oc>
            <no name=new type=no subtype=none>
               <av name=title descr="No Description">
                              <val value="how to set the exposure time and filter">
               </av>
               <av name=note descr="No Description">
                              <val value="The UFTI component below uses the I filter.  To change it move the highlight to the \"ufti component\" below and select the filter you want to use.\n\nThe UFTI component below uses a 500 second expsoure time, to get enough background signal to generate a flat field, at I\n\n">
               </av>
            </no>
            <oc name=new type=oc subtype=inst.UFTI>
               <av name=instPort descr="No Description">
                              <val value="East">
               </av>
               <av name=sourceMag descr="No Description">
                              <val value="BL">
               </av>
               <av name=.unique descr="No Description">
                              <val value="true">
               </av>
               <av name=filter descr="No Description">
                              <val value="I">
               </av>
               <av name=posAngle descr="No Description">
                              <val value="0.0">
               </av>
               <av name=coadds descr="No Description">
                              <val value="1">
               </av>
               <av name=instAper descr="No Description">
                              <val value="0.998">
                              <val value="-0.62">
                              <val value="0.0">
                              <val value="0.9">
               </av>
               <av name=exposureTime descr="No Description">
                              <val value="500.0">
               </av>
               <av name=readoutArea descr="No Description">
                              <val value="1024x1024">
               </av>
               <av name=title descr="No Description">
                              <val value="">
               </av>
               <av name=acqMode descr="No Description">
                              <val value="Normal+NDSTARE">
               </av>
               <av name=.version descr="No Description">
                              <val value="1.0">
               </av>
            </oc>
            <no name=new type=no subtype=none>
               <av name=title descr="No Description">
                              <val value="DR Recipe Selection">
               </av>
               <av name=note descr="No Description">
                              <val value="\n\nThis recipe generates a flat field that can be used by other data reduction recipes.">
               </av>
            </no>
            <oc name=new type=oc subtype=DRRecipe>
               <av name=ObjectRecipe descr="No Description">
                              <val value="SKY_FLAT">
               </av>
               <av name=DRRecipe descr="No Description">
                              <val value="SKY_FLAT">
               </av>
               <av name=DarkRecipe descr="No Description">
                              <val value="REDUCE_DARK">
               </av>
               <av name=SkyInGroup descr="No Description">
                              <val value="true">
               </av>
               <av name=.unique descr="No Description">
                              <val value="true">
               </av>
               <av name=ObjectInGroup descr="No Description">
                              <val value="true">
               </av>
               <av name=FlatRecipe descr="No Description">
                              <val value="REDUCE_FLAT">
               </av>
               <av name=DarkInGroup descr="No Description">
                              <val value="false">
               </av>
               <av name=SkyRecipe descr="No Description">
                              <val value="REDUCE_SKY">
               </av>
               <av name=ArcRecipe descr="No Description">
                              <val value="REDUCE_ARC">
               </av>
               <av name=BiasInGroup descr="No Description">
                              <val value="false">
               </av>
               <av name=title descr="No Description">
                              <val value="SKY_FLAT">
               </av>
               <av name=FlatInGroup descr="No Description">
                              <val value="false">
               </av>
               <av name=BiasRecipe descr="No Description">
                              <val value="REDUCE_BIAS">
               </av>
               <av name=ArcInGroup descr="No Description">
                              <val value="false">
               </av>
            </oc>
            <if name=new type=if subtype=none>
               <av name=.gui.collapsed descr="No Description">
                              <val value="false">
               </av>
               <no name=new type=no subtype=none>
                  <av name=title descr="No Description">
                                    <val value="change dark exp time here">
                  </av>
                  <av name=note descr="No Description">
                                    <val value="If you changed the exp time in the UFTI component then change it for the\ndark here\n\nNB :\nThe data reduction recipe will not work unless you have a dark with the right exposure time\n\nClick the reset to default button to ensure that your dark exposure time matches that set above.">
                  </av>
               </no>
               <ic name=new type=ic subtype=darkObs>
                  <av name=coadds descr="No Description">
                                    <val value="1">
                  </av>
                  <av name=repeatCount descr="No Description">
                                    <val value="1">
                  </av>
                  <av name=exposureTime descr="No Description">
                                    <val value="500.0">
                  </av>
               </ic>
               <ic name=new type=ic subtype=offset>
                  <av name=Offset8 descr="No Description">
                                    <val value="0.0">
                                    <val value="0.0">
                  </av>
                  <av name=Offset7 descr="No Description">
                                    <val value="22.0">
                                    <val value="-22.0">
                  </av>
                  <av name=Offset5 descr="No Description">
                                    <val value="-22.0">
                                    <val value="-20.0">
                  </av>
                  <av name=Offset3 descr="No Description">
                                    <val value="-20.0">
                                    <val value="22.0">
                  </av>
                  <av name=title descr="No Description">
                                    <val value="jitter_5_20as">
                  </av>
                  <av name=.gui.selectedOffsetPos descr="No Description">
                                    <val value="Offset8">
                  </av>
                  <av name=Offset1 descr="No Description">
                                    <val value="21.0">
                                    <val value="20.0">
                  </av>
                  <av name=offsetPositions descr="No Description">
                                    <val value="Offset8">
                                    <val value="Offset1">
                                    <val value="Offset3">
                                    <val value="Offset5">
                                    <val value="Offset7">
                  </av>
                  <av name=.gui.collapsed descr="No Description">
                                    <val value="false">
                  </av>
                  <ic name=new type=ic subtype=observe>
                     <av name=repeatCount descr="No Description">
                                          <val value="1">
                     </av>
                  </ic>
               </ic>
               <no name=new type=no subtype=none>
                  <av name=title descr="No Description">
                                    <val value="last offset">
                  </av>
                  <av name=note descr="No Description">
                                    <val value="This last offset is to send the telescope back to 00 after the observing is finished,">
                  </av>
               </no>
               <ic name=new type=ic subtype=offset>
                  <av name=offsetPositions descr="No Description">
                                    <val value="Offset0">
                  </av>
                  <av name=Offset0 descr="No Description">
                                    <val value="0.0">
                                    <val value="0.0">
                  </av>
                  <av name=.gui.selectedOffsetPos descr="No Description">
                                    <val value="Offset0">
                  </av>
               </ic>
            </if>
         </ob>
         <ob name=new type=ob subtype=none>
            <av name=title descr="No Description">
                        <val value="make_skyflat_for z filter">
            </av>
            <av name=standard descr="No Description">
                        <val value="false">
            </av>
            <av name=chainedToPrev descr="No Description">
                        <val value="false">
            </av>
            <av name=chainedToNext descr="No Description">
                        <val value="false">
            </av>
            <av name=.gui.collapsed descr="No Description">
                        <val value="true">
            </av>
            <no name=new type=no subtype=none>
               <av name=title descr="No Description">
                              <val value="useage">
               </av>
               <av name=note descr="No Description">
                              <val value="Use this observation to make some\nobservations of blank sky which can be used to generate flat fields if you\nare observing sources where the exposure time is too short to generate a flat field from the object jitter pattern,\n\nThe reduction recipe makes a flat that other reduction recipes can use">
               </av>
            </no>
            <no name=new type=no subtype=none>
               <av name=title descr="No Description">
                              <val value="target information">
               </av>
               <av name=note descr="No Description">
                              <val value="Select a piece of blank sky near your target\n\nput the coordinates into the target list,\n\nFor a step by step guide to how to use the position editor use \"help\".">
               </av>
            </no>
            <oc name=new type=oc subtype=targetList>
               <av name=Base descr="No Description">
                              <val value="Base">
                              <val value="enter your object name">
                              <val value="0:00:00">
                              <val value="0:00:00">
                              <val value="FK5 (J2000)">
               </av>
               <av name=.unique descr="No Description">
                              <val value="true">
               </av>
               <av name=.gui.selectedTelescopePos descr="No Description">
                              <val value="Base">
               </av>
            </oc>
            <no name=new type=no subtype=none>
               <av name=title descr="No Description">
                              <val value="how to set the exposure time and filter">
               </av>
               <av name=note descr="No Description">
                              <val value="The UFTI component below uses the z filter.  To change it move the highlight to the \"ufti component\" below and select the filter you want to use.\n\nThe UFTI component below uses a 250 second expsoure time, to get enough background signal to generate a flat field, at z\n\n">
               </av>
            </no>
            <oc name=new type=oc subtype=inst.UFTI>
               <av name=instPort descr="No Description">
                              <val value="East">
               </av>
               <av name=sourceMag descr="No Description">
                              <val value="BL">
               </av>
               <av name=.unique descr="No Description">
                              <val value="true">
               </av>
               <av name=filter descr="No Description">
                              <val value="Z">
               </av>
               <av name=posAngle descr="No Description">
                              <val value="0.0">
               </av>
               <av name=coadds descr="No Description">
                              <val value="1">
               </av>
               <av name=instAper descr="No Description">
                              <val value="0.998">
                              <val value="-0.62">
                              <val value="0.0">
                              <val value="1.033">
               </av>
               <av name=exposureTime descr="No Description">
                              <val value="250.0">
               </av>
               <av name=readoutArea descr="No Description">
                              <val value="1024x1024">
               </av>
               <av name=title descr="No Description">
                              <val value="">
               </av>
               <av name=acqMode descr="No Description">
                              <val value="Normal+NDSTARE">
               </av>
               <av name=.version descr="No Description">
                              <val value="1.0">
               </av>
            </oc>
            <no name=new type=no subtype=none>
               <av name=title descr="No Description">
                              <val value="DR Recipe Selection">
               </av>
               <av name=note descr="No Description">
                              <val value="\n\nThis recipe generates a flat field that can be used by other data reduction recipes.">
               </av>
            </no>
            <oc name=new type=oc subtype=DRRecipe>
               <av name=ObjectRecipe descr="No Description">
                              <val value="QUICK_LOOK">
               </av>
               <av name=DRRecipe descr="No Description">
                              <val value="SKY_FLAT">
               </av>
               <av name=DarkRecipe descr="No Description">
                              <val value="REDUCE_DARK">
               </av>
               <av name=SkyInGroup descr="No Description">
                              <val value="true">
               </av>
               <av name=.unique descr="No Description">
                              <val value="true">
               </av>
               <av name=ObjectInGroup descr="No Description">
                              <val value="true">
               </av>
               <av name=FlatRecipe descr="No Description">
                              <val value="REDUCE_FLAT">
               </av>
               <av name=DarkInGroup descr="No Description">
                              <val value="false">
               </av>
               <av name=SkyRecipe descr="No Description">
                              <val value="REDUCE_SKY">
               </av>
               <av name=ArcRecipe descr="No Description">
                              <val value="REDUCE_ARC">
               </av>
               <av name=BiasInGroup descr="No Description">
                              <val value="false">
               </av>
               <av name=title descr="No Description">
                              <val value="SKY_FLAT">
               </av>
               <av name=FlatInGroup descr="No Description">
                              <val value="false">
               </av>
               <av name=BiasRecipe descr="No Description">
                              <val value="REDUCE_BIAS">
               </av>
               <av name=ArcInGroup descr="No Description">
                              <val value="false">
               </av>
            </oc>
            <if name=new type=if subtype=none>
               <av name=.gui.collapsed descr="No Description">
                              <val value="false">
               </av>
               <no name=new type=no subtype=none>
                  <av name=title descr="No Description">
                                    <val value="change dark exp time here">
                  </av>
                  <av name=note descr="No Description">
                                    <val value="Take an appropriate dark before you start to do the jitter patterns. If you changed the exp time in the UFTI configuration then change it here too.\n\nNB :\nThe data reduction recipe will not work unless you have a dark with the right exposure time">
                  </av>
               </no>
               <ic name=new type=ic subtype=darkObs>
                  <av name=coadds descr="No Description">
                                    <val value="1">
                  </av>
                  <av name=repeatCount descr="No Description">
                                    <val value="1">
                  </av>
                  <av name=exposureTime descr="No Description">
                                    <val value="250.0">
                  </av>
               </ic>
               <ic name=new type=ic subtype=offset>
                  <av name=Offset8 descr="No Description">
                                    <val value="0.0">
                                    <val value="0.0">
                  </av>
                  <av name=Offset7 descr="No Description">
                                    <val value="22.0">
                                    <val value="-22.0">
                  </av>
                  <av name=Offset5 descr="No Description">
                                    <val value="-22.0">
                                    <val value="-20.0">
                  </av>
                  <av name=Offset3 descr="No Description">
                                    <val value="-20.0">
                                    <val value="22.0">
                  </av>
                  <av name=title descr="No Description">
                                    <val value="jitter_5_20as">
                  </av>
                  <av name=.gui.selectedOffsetPos descr="No Description">
                                    <val value="Offset8">
                  </av>
                  <av name=Offset1 descr="No Description">
                                    <val value="21.0">
                                    <val value="20.0">
                  </av>
                  <av name=offsetPositions descr="No Description">
                                    <val value="Offset8">
                                    <val value="Offset1">
                                    <val value="Offset3">
                                    <val value="Offset5">
                                    <val value="Offset7">
                  </av>
                  <av name=.gui.collapsed descr="No Description">
                                    <val value="false">
                  </av>
                  <ic name=new type=ic subtype=observe>
                     <av name=repeatCount descr="No Description">
                                          <val value="1">
                     </av>
                  </ic>
               </ic>
               <no name=new type=no subtype=none>
                  <av name=title descr="No Description">
                                    <val value="last offset">
                  </av>
                  <av name=note descr="No Description">
                                    <val value="This last offset is to send the telescope back to 00 after all the data is taken">
                  </av>
               </no>
               <ic name=new type=ic subtype=offset>
                  <av name=offsetPositions descr="No Description">
                                    <val value="Offset0">
                  </av>
                  <av name=Offset0 descr="No Description">
                                    <val value="0.0">
                                    <val value="0.0">
                  </av>
                  <av name=.gui.selectedOffsetPos descr="No Description">
                                    <val value="Offset0">
                  </av>
               </ic>
            </if>
         </ob>
      </lf>
      <lf name=new type=lf subtype=none>
         <av name=title descr="No Description">
                  <val value="CGS4 library">
         </av>
         <av name=.gui.collapsed descr="No Description">
                  <val value="false">
         </av>
         <no name=new type=no subtype=none>
            <av name=title descr="No Description">
                        <val value="In this folder">
            </av>
            <av name=note descr="No Description">
                        <val value="There are a selection of standard CGs4 sequences to use.\n\nSelect the ones you need and copy them to your science programme where you can fill in the details of target coordinates and CGS4 setup details.\n\nRemember to include and array_tests to check out the array performance at the start of the night, and also an end_night, to run after you have finished observing.\n\nIf you want to observe in the \"traditional\" CGS4 mode that is nodding the target between two positions on the slit, like a \"quad slide exec\" used to do then use one of the sequences that are called \"nod along slit\"\n\nSequences called \"nod to blank sky\" assume that you really are nodding to blank sky e.g for extended sources.  The data reduction treats the blank sky frames differently\n\n">
            </av>
         </no>
         <ob name=new type=ob subtype=none>
            <av name=title descr="No Description">
                        <val value="array tests">
            </av>
            <av name=standard descr="No Description">
                        <val value="false">
            </av>
            <av name=chainedToPrev descr="No Description">
                        <val value="false">
            </av>
            <av name=chainedToNext descr="No Description">
                        <val value="false">
            </av>
            <av name=.gui.collapsed descr="No Description">
                        <val value="true">
            </av>
            <no name=new type=no subtype=none>
               <av name=title descr="No Description">
                              <val value="array tests info">
               </av>
               <av name=note descr="No Description">
                              <val value="This does 2 bias frames and 4 darks with differing exposure times to checkout the array performance.\n\nYou should always run this once at the start of the night.\n\nCGS4 is set to a default configuration below - the action of the dark/bias observations puts the blanks in.">
               </av>
            </no>
            <oc name=new type=oc subtype=inst.CGS4>
               <av name=order descr="No Description">
                              <val value="1">
               </av>
               <av name=filter descr="No Description">
                              <val value="B2">
               </av>
               <av name=coadds descr="No Description">
                              <val value="6">
               </av>
               <av name=polariser descr="No Description">
                              <val value="none">
               </av>
               <av name=acqMode descr="No Description">
                              <val value="STARE">
               </av>
               <av name=disperser descr="No Description">
                              <val value="40lpmm">
               </av>
               <av name=.version descr="No Description">
                              <val value="1.0">
               </av>
               <av name=sourceMag descr="No Description">
                              <val value="none">
               </av>
               <av name=.unique descr="No Description">
                              <val value="true">
               </av>
               <av name=exposureTime descr="No Description">
                              <val value="0.12">
               </av>
               <av name=mask descr="No Description">
                              <val value="1pixel">
               </av>
               <av name=sampling descr="No Description">
                              <val value="1x1">
               </av>
               <av name=cvfOffset descr="No Description">
                              <val value="0.0">
               </av>
               <av name=instPort descr="No Description">
                              <val value="South">
               </av>
               <av name=centralWavelength descr="No Description">
                              <val value="2.1">
               </av>
               <av name=instAper descr="No Description">
                              <val value="0.0">
                              <val value="0.0">
                              <val value="0.0">
                              <val value="2.1">
               </av>
               <av name=neutralDensity descr="No Description">
                              <val value="0">
               </av>
            </oc>
            <oc name=new type=oc subtype=DRRecipe>
               <av name=ObjectRecipe descr="No Description">
                              <val value="ARRAY_TESTS">
               </av>
               <av name=DarkRecipe descr="No Description">
                              <val value="ARRAY_TESTS">
               </av>
               <av name=SkyInGroup descr="No Description">
                              <val value="false">
               </av>
               <av name=.unique descr="No Description">
                              <val value="true">
               </av>
               <av name=ObjectInGroup descr="No Description">
                              <val value="false">
               </av>
               <av name=FlatRecipe descr="No Description">
                              <val value="REDUCE_FLAT">
               </av>
               <av name=DarkInGroup descr="No Description">
                              <val value="true">
               </av>
               <av name=SkyRecipe descr="No Description">
                              <val value="REDUCE_SKY">
               </av>
               <av name=ArcRecipe descr="No Description">
                              <val value="REDUCE_ARC">
               </av>
               <av name=BiasInGroup descr="No Description">
                              <val value="true">
               </av>
               <av name=title descr="No Description">
                              <val value="ARRAY_TESTS">
               </av>
               <av name=FlatInGroup descr="No Description">
                              <val value="false">
               </av>
               <av name=BiasRecipe descr="No Description">
                              <val value="ARRAY_TESTS">
               </av>
               <av name=ArcInGroup descr="No Description">
                              <val value="false">
               </av>
            </oc>
            <if name=new type=if subtype=none>
               <av name=.gui.collapsed descr="No Description">
                              <val value="false">
               </av>
               <ic name=new type=ic subtype=biasObs>
                  <av name=coadds descr="No Description">
                                    <val value="3">
                  </av>
                  <av name=repeatCount descr="No Description">
                                    <val value="2">
                  </av>
                  <av name=exposureTime descr="No Description">
                                    <val value="10.0">
                  </av>
               </ic>
               <ic name=new type=ic subtype=instCGS4>
                  <av name=exposureTimeIter descr="No Description">
                                    <val value="">
                  </av>
                  <av name=iterConfigList descr="No Description">
                                    <val value="acqModeIter">
                                    <val value="exposureTimeIter">
                  </av>
                  <av name=acqModeIter descr="No Description">
                                    <val value="NDSTARE">
                  </av>
               </ic>
               <ic name=new type=ic subtype=darkObs>
                  <av name=coadds descr="No Description">
                                    <val value="1">
                  </av>
                  <av name=repeatCount descr="No Description">
                                    <val value="2">
                  </av>
                  <av name=exposureTime descr="No Description">
                                    <val value="1.0">
                  </av>
               </ic>
               <ic name=new type=ic subtype=instCGS4>
                  <av name=iterConfigList descr="No Description">
                                    <val value="acqModeIter">
                  </av>
                  <av name=acqModeIter descr="No Description">
                                    <val value="STARE">
                  </av>
               </ic>
               <ic name=new type=ic subtype=darkObs>
                  <av name=coadds descr="No Description">
                                    <val value="1">
                  </av>
                  <av name=repeatCount descr="No Description">
                                    <val value="1">
                  </av>
                  <av name=exposureTime descr="No Description">
                                    <val value="5.0">
                  </av>
               </ic>
               <ic name=new type=ic subtype=darkObs>
                  <av name=coadds descr="No Description">
                                    <val value="1">
                  </av>
                  <av name=repeatCount descr="No Description">
                                    <val value="1">
                  </av>
                  <av name=exposureTime descr="No Description">
                                    <val value="60.0">
                  </av>
               </ic>
               <ic name=new type=ic subtype=instCGS4>
                  <av name=iterConfigList descr="No Description">
                                    <val value="acqModeIter">
                  </av>
                  <av name=acqModeIter descr="No Description">
                                    <val value="NDSTARE">
                  </av>
               </ic>
               <ic name=new type=ic subtype=darkObs>
                  <av name=coadds descr="No Description">
                                    <val value="1">
                  </av>
                  <av name=repeatCount descr="No Description">
                                    <val value="1">
                  </av>
                  <av name=exposureTime descr="No Description">
                                    <val value="100">
                  </av>
               </ic>
            </if>
         </ob>
         <ob name=new type=ob subtype=none>
            <av name=title descr="No Description">
                        <val value="end of night">
            </av>
            <av name=standard descr="No Description">
                        <val value="false">
            </av>
            <av name=chainedToPrev descr="No Description">
                        <val value="false">
            </av>
            <av name=chainedToNext descr="No Description">
                        <val value="false">
            </av>
            <av name=.gui.collapsed descr="No Description">
                        <val value="true">
            </av>
            <oc name=new type=oc subtype=inst.CGS4>
               <av name=order descr="No Description">
                              <val value="1">
               </av>
               <av name=filter descr="No Description">
                              <val value="B2">
               </av>
               <av name=coadds descr="No Description">
                              <val value="2">
               </av>
               <av name=polariser descr="No Description">
                              <val value="none">
               </av>
               <av name=acqMode descr="No Description">
                              <val value="NDSTARE">
               </av>
               <av name=disperser descr="No Description">
                              <val value="40lpmm">
               </av>
               <av name=.version descr="No Description">
                              <val value="1.0">
               </av>
               <av name=sourceMag descr="No Description">
                              <val value="none">
               </av>
               <av name=.unique descr="No Description">
                              <val value="true">
               </av>
               <av name=exposureTime descr="No Description">
                              <val value="4.0">
               </av>
               <av name=mask descr="No Description">
                              <val value="1pixel">
               </av>
               <av name=sampling descr="No Description">
                              <val value="1x1">
               </av>
               <av name=cvfOffset descr="No Description">
                              <val value="0.0">
               </av>
               <av name=instPort descr="No Description">
                              <val value="South">
               </av>
               <av name=centralWavelength descr="No Description">
                              <val value="2.1">
               </av>
               <av name=instAper descr="No Description">
                              <val value="-0.55">
                              <val value="7.62">
                              <val value="0.0">
                              <val value="2.1">
               </av>
               <av name=neutralDensity descr="No Description">
                              <val value="0">
               </av>
            </oc>
            <if name=new type=if subtype=none>
               <ic name=new type=ic subtype=darkObs>
                  <av name=coadds descr="No Description">
                                    <val value="1">
                  </av>
                  <av name=repeatCount descr="No Description">
                                    <val value="1">
                  </av>
                  <av name=exposureTime descr="No Description">
                                    <val value="1.0">
                  </av>
               </ic>
            </if>
         </ob>
         <ob name=new type=ob subtype=none>
            <av name=title descr="No Description">
                        <val value="end of night with emissivity check">
            </av>
            <av name=standard descr="No Description">
                        <val value="false">
            </av>
            <av name=chainedToPrev descr="No Description">
                        <val value="false">
            </av>
            <av name=chainedToNext descr="No Description">
                        <val value="false">
            </av>
            <av name=.gui.collapsed descr="No Description">
                        <val value="true">
            </av>
            <av name=.gui.selected descr="No Description">
                        <val value="false">
            </av>
            <no name=new type=no subtype=none>
               <av name=title descr="No Description">
                              <val value="emissivity check">
               </av>
               <av name=note descr="No Description">
                              <val value="This end of night observation does an emissivity check by measuring the sky and the dome flux in the thermal IR.  \n\nThe last step is to blank off the CGS4 array, and take a very short dark.\n\nMake sure you are pointed at blank sky before you start.\n\n">
               </av>
            </no>
            <oc name=new type=oc subtype=inst.CGS4>
               <av name=order descr="No Description">
                              <val value="16">
               </av>
               <av name=filter descr="No Description">
                              <val value="open">
               </av>
               <av name=coadds descr="No Description">
                              <val value="2">
               </av>
               <av name=polariser descr="No Description">
                              <val value="none">
               </av>
               <av name=acqMode descr="No Description">
                              <val value="NDSTARE">
               </av>
               <av name=disperser descr="No Description">
                              <val value="echelle">
               </av>
               <av name=.version descr="No Description">
                              <val value="1.0">
               </av>
               <av name=sourceMag descr="No Description">
                              <val value="none">
               </av>
               <av name=.unique descr="No Description">
                              <val value="true">
               </av>
               <av name=exposureTime descr="No Description">
                              <val value="4.0">
               </av>
               <av name=mask descr="No Description">
                              <val value="2pixel">
               </av>
               <av name=sampling descr="No Description">
                              <val value="1x1">
               </av>
               <av name=cvfOffset descr="No Description">
                              <val value="0.015">
               </av>
               <av name=instPort descr="No Description">
                              <val value="South">
               </av>
               <av name=centralWavelength descr="No Description">
                              <val value="3.5">
               </av>
               <av name=instAper descr="No Description">
                              <val value="-0.03">
                              <val value="9.95">
                              <val value="0.0">
                              <val value="3.5">
               </av>
               <av name=neutralDensity descr="No Description">
                              <val value="0">
               </av>
            </oc>
            <if name=new type=if subtype=none>
               <ic name=new type=ic subtype=CGS4calUnitObs>
                  <av name=flatSampling descr="No Description">
                                    <val value="1x1">
                  </av>
                  <av name=filter descr="No Description">
                                    <val value="open">
                  </av>
                  <av name=coadds descr="No Description">
                                    <val value="30">
                  </av>
                  <av name=calType descr="No Description">
                                    <val value="Flat">
                  </av>
                  <av name=repeatCount descr="No Description">
                                    <val value="1">
                  </av>
                  <av name=exposureTime descr="No Description">
                                    <val value="0.2">
                  </av>
                  <av name=cvfWavelength descr="No Description">
                                    <val value="0.0">
                  </av>
                  <av name=acqMode descr="No Description">
                                    <val value="NDSTARE">
                  </av>
                  <av name=lamp descr="No Description">
                                    <val value="Black Body (5.0)">
                  </av>
               </ic>
               <ic name=new type=ic subtype=instCGS4>
                  <av name=exposureTimeIter descr="No Description">
                                    <val value="12">
                  </av>
                  <av name=coaddsIter descr="No Description">
                                    <val value="3">
                  </av>
                  <av name=iterConfigList descr="No Description">
                                    <val value="exposureTimeIter">
                                    <val value="coaddsIter">
                  </av>
               </ic>
               <ic name=new type=ic subtype=sky>
                  <av name=repeatCount descr="No Description">
                                    <val value="1">
                  </av>
               </ic>
               <ic name=new type=ic subtype=instCGS4>
                  <av name=exposureTimeIter descr="No Description">
                                    <val value="2">
                  </av>
                  <av name=coaddsIter descr="No Description">
                                    <val value="10">
                  </av>
                  <av name=iterConfigList descr="No Description">
                                    <val value="exposureTimeIter">
                                    <val value="coaddsIter">
                  </av>
               </ic>
               <no name=new type=no subtype=none>
                  <av name=title descr="No Description">
                                    <val value="Now point at the dome">
                  </av>
                  <av name=note descr="No Description">
                                    <val value="The sequence will pause at this point so you can point at the dome.">
                  </av>
               </no>
               <ic name=new type=ic subtype=observe>
                  <av name=repeatCount descr="No Description">
                                    <val value="1">
                  </av>
                  <av name=title descr="No Description">
                                    <val value="DOME">
                  </av>
               </ic>
               <ic name=new type=ic subtype=darkObs>
                  <av name=coadds descr="No Description">
                                    <val value="1">
                  </av>
                  <av name=repeatCount descr="No Description">
                                    <val value="1">
                  </av>
                  <av name=exposureTime descr="No Description">
                                    <val value="1">
                  </av>
               </ic>
            </if>
         </ob>
         <ob name=new type=ob subtype=none>
            <av name=title descr="No Description">
                        <val value="flat and an arc">
            </av>
            <av name=standard descr="No Description">
                        <val value="false">
            </av>
            <av name=chainedToPrev descr="No Description">
                        <val value="false">
            </av>
            <av name=chainedToNext descr="No Description">
                        <val value="false">
            </av>
            <av name=.gui.collapsed descr="No Description">
                        <val value="true">
            </av>
            <no name=new type=no subtype=none>
               <av name=title descr="No Description">
                              <val value="about this observation">
               </av>
               <av name=note descr="No Description">
                              <val value="This observation does just a flat and an arc.\n">
               </av>
            </no>
            <no name=new type=no subtype=none>
               <av name=title descr="No Description">
                              <val value="cgs4 setup">
               </av>
               <av name=note descr="No Description">
                              <val value="For detailed advice about CGS4 see the web page at\nhttp://www.jach.hawaii.edu/JACpublic/UKIRT/instruments/cgs4/handbook.html\n\nEnter your desired CGS4 setup into the component below.  Make sure you choose the same settings for grating, wavelength, order, slit width and position angle as for your sources.\n\nNote that if you enter a central wavelength and choose a grating the order will be automatically\nselected. You may over-ride this if you wish, but seek advice on non-optimum orders from your support scientist.\n\nThere is feedback showing what the wavelength range and spectral resolution with the 1-pixel wide slit would be.\n\nEnter the slit width in pixels that you want to use.  Slit widths wider than 1-pixel may result in lower spectral resolution, depending on the seeing.  \n\nSlit position angles on the sky should fall in \nthe range 7 to -173 degrees EAST of NORTH\n\nIf you select a source magnitude range the tool will enter a recommended exposure time and number of coadds at each sampling position.  You may enter your own value instead if you prefer. If the exposure time is short (less than 1sec) consider using STARE mode instead.\n\nSampling is specified as \"Oversampling factor\" x\n\"number of pixels\" the spectrum is stepped over. So 2 x 2 sampling is Nyquist sampling stepped over 2 pixels giving a total of 4 independant measurements of the spectrum, 2 at each data point in the final spectrum.\n\n ">
               </av>
            </no>
            <oc name=new type=oc subtype=inst.CGS4>
               <av name=order descr="No Description">
                              <val value="1">
               </av>
               <av name=posAngle descr="No Description">
                              <val value="0">
               </av>
               <av name=filter descr="No Description">
                              <val value="B2">
               </av>
               <av name=coadds descr="No Description">
                              <val value="15">
               </av>
               <av name=polariser descr="No Description">
                              <val value="none">
               </av>
               <av name=acqMode descr="No Description">
                              <val value="NDSTARE">
               </av>
               <av name=disperser descr="No Description">
                              <val value="40lpmm">
               </av>
               <av name=.version descr="No Description">
                              <val value="1.0">
               </av>
               <av name=sourceMag descr="No Description">
                              <val value="3-4">
               </av>
               <av name=.unique descr="No Description">
                              <val value="true">
               </av>
               <av name=exposureTime descr="No Description">
                              <val value="0.35">
               </av>
               <av name=mask descr="No Description">
                              <val value="1pixel">
               </av>
               <av name=sampling descr="No Description">
                              <val value="2x2">
               </av>
               <av name=cvfOffset descr="No Description">
                              <val value="0.0">
               </av>
               <av name=instPort descr="No Description">
                              <val value="South">
               </av>
               <av name=centralWavelength descr="No Description">
                              <val value="2.1">
               </av>
               <av name=instAper descr="No Description">
                              <val value="0.0">
                              <val value="0.0">
                              <val value="0.0">
                              <val value="2.1">
               </av>
               <av name=neutralDensity descr="No Description">
                              <val value="0">
               </av>
            </oc>
            <no name=new type=no subtype=none>
               <av name=title descr="No Description">
                              <val value="DR recipe">
               </av>
               <av name=note descr="No Description">
                              <val value="The item below specifies the standard reduction recipe for flats and arcs.  This will ensure that the reduced flat and arc is available to ORACDR for reduction of your source data.">
               </av>
            </no>
            <oc name=new type=oc subtype=DRRecipe>
               <av name=ObjectRecipe descr="No Description">
                              <val value="QUICK_LOOK">
               </av>
               <av name=DarkRecipe descr="No Description">
                              <val value="REDUCE_DARK">
               </av>
               <av name=SkyInGroup descr="No Description">
                              <val value="true">
               </av>
               <av name=.unique descr="No Description">
                              <val value="true">
               </av>
               <av name=ObjectInGroup descr="No Description">
                              <val value="true">
               </av>
               <av name=FlatRecipe descr="No Description">
                              <val value="REDUCE_FLAT">
               </av>
               <av name=DarkInGroup descr="No Description">
                              <val value="false">
               </av>
               <av name=SkyRecipe descr="No Description">
                              <val value="REDUCE_SKY">
               </av>
               <av name=ArcRecipe descr="No Description">
                              <val value="REDUCE_ARC">
               </av>
               <av name=BiasInGroup descr="No Description">
                              <val value="false">
               </av>
               <av name=FlatInGroup descr="No Description">
                              <val value="false">
               </av>
               <av name=BiasRecipe descr="No Description">
                              <val value="REDUCE_BIAS">
               </av>
               <av name=ArcInGroup descr="No Description">
                              <val value="false">
               </av>
            </oc>
            <if name=new type=if subtype=none>
               <no name=new type=no subtype=none>
                  <av name=title descr="No Description">
                                    <val value="flat - how to use">
                  </av>
                  <av name=note descr="No Description">
                                    <val value="CLICK ON THE \"DEFAULT\" BUTTON \nto get sensible default values for the flat lamp and its aperture, the order sorting filter and the exposure time, based on the settings in the CGS4 component above.\n\nThese may all be changed if you wish.  To get the default values back, click on default again.\n\nTo get a single higher signal to noise flat increase the number of coadds\n\nTo take several flats for later addition increase the number next to \"observe\"">
                  </av>
               </no>
               <ic name=new type=ic subtype=CGS4calUnitObs>
                  <av name=flatSampling descr="No Description">
                                    <val value="1x1">
                  </av>
                  <av name=filter descr="No Description">
                                    <val value="B2">
                  </av>
                  <av name=coadds descr="No Description">
                                    <val value="5">
                  </av>
                  <av name=calType descr="No Description">
                                    <val value="Flat">
                  </av>
                  <av name=repeatCount descr="No Description">
                                    <val value="1">
                  </av>
                  <av name=exposureTime descr="No Description">
                                    <val value="1">
                  </av>
                  <av name=cvfWavelength descr="No Description">
                                    <val value="0.0">
                  </av>
                  <av name=acqMode descr="No Description">
                                    <val value="NDSTARE">
                  </av>
                  <av name=lamp descr="No Description">
                                    <val value="Black Body (1.3)">
                  </av>
               </ic>
               <no name=new type=no subtype=none>
                  <av name=title descr="No Description">
                                    <val value="arc - how to use">
                  </av>
                  <av name=note descr="No Description">
                                    <val value="CLICK ON THE \"DEFAULT\" BUTTON \nto get sensible default values for the arc lamp, the order sorting filter and the exposure time, based on the CGS4 component above.\n\nThese may all be changed if you wish.  To get the default values back, click on default again.\n\nNote that for wavelengths longer than 2.8um the default blocking filter is set so that arcs are obtained in a x 2 higher order.  (the glass of the\nlamps does not transmit thermal lines).\n\nTo get a single higher signal to noise arc increase the number of coadds\n\nTo take several arcs for later addition increase the number next to \"observe\"\n\nto do two or more arcs using different arc lamps :\nMove the yellow highlight to the observe eye labelled Arc below. Under \"edit\" at the top of the component click on \"copy\". Then click on \"paste\" to add extra arc observations as many times as you need.  Go into each arc component - click on default to get a sensible order sorting filter, then select a different arc lamp or other item.\n">
                  </av>
               </no>
               <ic name=new type=ic subtype=CGS4calUnitObs>
                  <av name=flatSampling descr="No Description">
                                    <val value="1x1">
                  </av>
                  <av name=filter descr="No Description">
                                    <val value="B2">
                  </av>
                  <av name=coadds descr="No Description">
                                    <val value="5">
                  </av>
                  <av name=calType descr="No Description">
                                    <val value="Arc">
                  </av>
                  <av name=repeatCount descr="No Description">
                                    <val value="1">
                  </av>
                  <av name=exposureTime descr="No Description">
                                    <val value="0.5">
                  </av>
                  <av name=cvfWavelength descr="No Description">
                                    <val value="0.0">
                  </av>
                  <av name=acqMode descr="No Description">
                                    <val value="NDSTARE">
                  </av>
                  <av name=lamp descr="No Description">
                                    <val value="Argon Arc">
                  </av>
               </ic>
            </if>
         </ob>
         <ob name=new type=ob subtype=none>
            <av name=title descr="No Description">
                        <val value="flat,arc and std. star">
            </av>
            <av name=standard descr="No Description">
                        <val value="true">
            </av>
            <av name=chainedToPrev descr="No Description">
                        <val value="false">
            </av>
            <av name=chainedToNext descr="No Description">
                        <val value="false">
            </av>
            <av name=.gui.collapsed descr="No Description">
                        <val value="true">
            </av>
            <no name=new type=no subtype=none>
               <av name=title descr="No Description">
                              <val value="about this observation">
               </av>
               <av name=note descr="No Description">
                              <val value="This observation includes a flat, an arc and a\nstandard star\n\nThe standard is flagged as a standard in the observation component above.\n\nORAC-DR uses this flag to idnetify standards during reduction so that they may be used with subsequent observations">
               </av>
            </no>
            <no name=new type=no subtype=none>
               <av name=title descr="No Description">
                              <val value="standard coordinates information">
               </av>
               <av name=note descr="No Description">
                              <val value="enter your standard star (or target) co-ordinates into the target list below.  Note that although this is called a \"target list\" it only allows you to specify ONE astronomical target\n\nWhen you have done so you can use the \"position editor\" to  select a guide star, to fine tune the co-ordinate based on a sky survey picture, examine offset locations on the field etc.\n\nFor a step by step guide to how to use the position editor use \"help\".\n\nIF YOU WANT TO SELECT YOUR STANDARD STAR BY HAVING THE TSS SEARCH WITH THE TELESCOPE \"NEARSTAR\" IN REAL TIME THEN ENTER YOUR TARGET CO-ORDINATES HERE, AND DO NOT SPECIFY A GUIDE STAR.  AFTER THESE ARE SENT TO THE TELESCOPE THERE WILL BE\nA PAUSE FOR THE TSS TO SLEW TO THE CHOSEN NEARBY STAR.\n">
               </av>
            </no>
            <oc name=new type=oc subtype=targetList>
               <av name=Base descr="No Description">
                              <val value="Base">
                              <val value="enter your object name">
                              <val value="0:00:00">
                              <val value="0:00:00">
                              <val value="FK5 (J2000)">
               </av>
               <av name=.unique descr="No Description">
                              <val value="true">
               </av>
               <av name=.gui.selectedTelescopePos descr="No Description">
                              <val value="Base">
               </av>
            </oc>
            <no name=new type=no subtype=none>
               <av name=title descr="No Description">
                              <val value="cgs4 setup">
               </av>
               <av name=note descr="No Description">
                              <val value="For detailed advice about CGS4 see the web page at\nhttp://www.jach.hawaii.edu/JACpublic/UKIRT/instruments/cgs4/handbook.html\n\nEnter your desired CGS4 setup into the component below.  Make sure you choose the same settings for grating, wavelength, order, slit width and position angle for your STANDARD STAR as for your sources.\n\nNote that if you enter a central wavelength and choose a grating the order will be automatically\nselected. You may over-ride this if you wish, but seek advice on non-optimum orders from your support scientist.\n\nThere is feedback showing what the wavelength range and spectral resolution with the 1-pixel wide slit would be.\n\nEnter the slit width in pixels that you want to use.  Slit widths wider than 1-pixel may result in lower spectral resolution, depending on the seeing.  \n\nSlit position angles on the sky should fall in \nthe range 7 to -173 degrees EAST of NORTH\n\nIf you select a source magnitude range the tool will enter a recommended exposure time and number of coadds at each sampling position.  You may enter your own value instead if you prefer. If the exposure time is short (less than 1sec) consider using STARE mode instead.\n\nSampling is specified as \"Oversampling factor\" x\n\"number of pixels\" the spectrum is stepped over. So 2 x 2 sampling is Nyquist sampling stepped over 2 pixels giving a total of 4 independant measurements of the spectrum, 2 at each data point in the final spectrum.\n\n ">
               </av>
            </no>
            <oc name=new type=oc subtype=inst.CGS4>
               <av name=order descr="No Description">
                              <val value="1">
               </av>
               <av name=posAngle descr="No Description">
                              <val value="0">
               </av>
               <av name=filter descr="No Description">
                              <val value="B2">
               </av>
               <av name=coadds descr="No Description">
                              <val value="1">
               </av>
               <av name=polariser descr="No Description">
                              <val value="none">
               </av>
               <av name=acqMode descr="No Description">
                              <val value="NDSTARE">
               </av>
               <av name=disperser descr="No Description">
                              <val value="40lpmm">
               </av>
               <av name=.version descr="No Description">
                              <val value="1.0">
               </av>
               <av name=sourceMag descr="No Description">
                              <val value="7-8">
               </av>
               <av name=.unique descr="No Description">
                              <val value="true">
               </av>
               <av name=exposureTime descr="No Description">
                              <val value="14.0">
               </av>
               <av name=mask descr="No Description">
                              <val value="1pixel">
               </av>
               <av name=sampling descr="No Description">
                              <val value="2x2">
               </av>
               <av name=cvfOffset descr="No Description">
                              <val value="0.0">
               </av>
               <av name=instPort descr="No Description">
                              <val value="South">
               </av>
               <av name=centralWavelength descr="No Description">
                              <val value="2.1">
               </av>
               <av name=instAper descr="No Description">
                              <val value="-0.55">
                              <val value="7.62">
                              <val value="0.0">
                              <val value="2.1">
               </av>
               <av name=neutralDensity descr="No Description">
                              <val value="0">
               </av>
            </oc>
            <no name=new type=no subtype=none>
               <av name=title descr="No Description">
                              <val value="DR Recipe Selection">
               </av>
               <av name=note descr="No Description">
                              <val value="The data reduction recipe specified below uses the most basic data reduction recipe for speed.  (this is a good idea if your exposure time short).\n\n\n">
               </av>
            </no>
            <oc name=new type=oc subtype=DRRecipe>
               <av name=ObjectRecipe descr="No Description">
                              <val value="STANDARD_STAR">
               </av>
               <av name=DRRecipe descr="No Description">
                              <val value="JITTER5_SELF_FLAT">
               </av>
               <av name=DarkRecipe descr="No Description">
                              <val value="REDUCE_DARK">
               </av>
               <av name=SkyInGroup descr="No Description">
                              <val value="false">
               </av>
               <av name=.unique descr="No Description">
                              <val value="true">
               </av>
               <av name=ObjectInGroup descr="No Description">
                              <val value="true">
               </av>
               <av name=FlatRecipe descr="No Description">
                              <val value="REDUCE_FLAT">
               </av>
               <av name=DarkInGroup descr="No Description">
                              <val value="false">
               </av>
               <av name=SkyRecipe descr="No Description">
                              <val value="REDUCE_SKY">
               </av>
               <av name=ArcRecipe descr="No Description">
                              <val value="REDUCE_ARC">
               </av>
               <av name=BiasInGroup descr="No Description">
                              <val value="false">
               </av>
               <av name=title descr="No Description">
                              <val value="STANDARD_STAR">
               </av>
               <av name=FlatInGroup descr="No Description">
                              <val value="false">
               </av>
               <av name=BiasRecipe descr="No Description">
                              <val value="REDUCE_BIAS">
               </av>
               <av name=ArcInGroup descr="No Description">
                              <val value="false">
               </av>
            </oc>
            <if name=new type=if subtype=none>
               <av name=.gui.collapsed descr="No Description">
                              <val value="false">
               </av>
               <no name=new type=no subtype=none>
                  <av name=title descr="No Description">
                                    <val value="flat - how to use">
                  </av>
                  <av name=note descr="No Description">
                                    <val value="CLICK ON THE \"DEFAULT\" BUTTON \nto get sensible defaul values for the flat lamp and its aperture, the order sorting filter and the exposure time.  These may all be changed if you wish.  To get the default values back, click on default again.\n\nTo get a single higher signal to noise flat increase the number of coadds\n\nTo take several flats for later addition increase the number next to \"observe\"">
                  </av>
               </no>
               <ic name=new type=ic subtype=CGS4calUnitObs>
                  <av name=flatSampling descr="No Description">
                                    <val value="1x1">
                  </av>
                  <av name=filter descr="No Description">
                                    <val value="B2">
                  </av>
                  <av name=coadds descr="No Description">
                                    <val value="5">
                  </av>
                  <av name=calType descr="No Description">
                                    <val value="Flat">
                  </av>
                  <av name=repeatCount descr="No Description">
                                    <val value="1">
                  </av>
                  <av name=exposureTime descr="No Description">
                                    <val value="">
                  </av>
                  <av name=cvfWavelength descr="No Description">
                                    <val value="0.0">
                  </av>
                  <av name=acqMode descr="No Description">
                                    <val value="NDSTARE">
                  </av>
                  <av name=lamp descr="No Description">
                                    <val value="Black Body (1.3)">
                  </av>
               </ic>
               <no name=new type=no subtype=none>
                  <av name=title descr="No Description">
                                    <val value="arc - how to use">
                  </av>
                  <av name=note descr="No Description">
                                    <val value="CLICK ON THE \"DEFAULT\" BUTTON \nto get sensible default values for the arc lamp, the order sorting filter and the exposure time.  These may all be changed if you wish.  To get the default values back, click on default again.\n\nNote that for wavelengths longer than 3.?um the default blocking filter is set so that arcs are obtained in a x 2 higher order.  (the glass of the\nlamps does not transmit thermal lines).\n\nTo get a single higher signal to noise arc increase the number of coadds\n\nTo take several arcs for later addition increase the number next to \"observe\"\n\nto do two or more arcs using different arc lamps :\nMove the yellow highlight to the observe eye labelled Arc below. Under \"edit\" at the top of the component click on \"copy\". Then click on \"paste\" to add extra arc observations as many times as you need.  Go into each arc component - click on default to get a sensible order sorting filter, then select a different arc lamp or other item.\n">
                  </av>
               </no>
               <ic name=new type=ic subtype=CGS4calUnitObs>
                  <av name=flatSampling descr="No Description">
                                    <val value="1x1">
                  </av>
                  <av name=filter descr="No Description">
                                    <val value="B2">
                  </av>
                  <av name=coadds descr="No Description">
                                    <val value="5">
                  </av>
                  <av name=calType descr="No Description">
                                    <val value="Arc">
                  </av>
                  <av name=repeatCount descr="No Description">
                                    <val value="1">
                  </av>
                  <av name=exposureTime descr="No Description">
                                    <val value="">
                  </av>
                  <av name=cvfWavelength descr="No Description">
                                    <val value="0.0">
                  </av>
                  <av name=acqMode descr="No Description">
                                    <val value="NDSTARE">
                  </av>
                  <av name=lamp descr="No Description">
                                    <val value="Argon Arc">
                  </av>
               </ic>
               <no name=new type=no subtype=none>
                  <av name=title descr="No Description">
                                    <val value="set the number of repeats">
                  </av>
                  <av name=note descr="No Description">
                                    <val value="To repeat the quads more than once on your standard star , use the repeat item below to specify how many times you want to repeat it.\n\n">
                  </av>
               </no>
               <ic name=new type=ic subtype=repeat>
                  <av name=repeatCount descr="No Description">
                                    <val value="2">
                  </av>
                  <av name=title descr="No Description">
                                    <val value="Repeat 2X">
                  </av>
                  <av name=.gui.collapsed descr="No Description">
                                    <val value="false">
                  </av>
                  <no name=new type=no subtype=none>
                     <av name=title descr="No Description">
                                          <val value="offset info">
                     </av>
                     <av name=note descr="No Description">
                                          <val value="this nods 30 rows along the slit\n\nOffsets are enetered in the co-ordinate frame of the slit\n\np = along the slit\nq = perpendicular to the slit">
                     </av>
                  </no>
                  <ic name=new type=ic subtype=offset>
                     <av name=.gui.selectedOffsetPos descr="No Description">
                                          <val value="Offset2">
                     </av>
                     <av name=Offset3 descr="No Description">
                                          <val value="0.0">
                                          <val value="0.0">
                     </av>
                     <av name=Offset2 descr="No Description">
                                          <val value="0.0">
                                          <val value="11.74">
                     </av>
                     <av name=Offset1 descr="No Description">
                                          <val value="0.0">
                                          <val value="11.74">
                     </av>
                     <av name=offsetPositions descr="No Description">
                                          <val value="Offset0">
                                          <val value="Offset1">
                                          <val value="Offset2">
                                          <val value="Offset3">
                     </av>
                     <av name=Offset0 descr="No Description">
                                          <val value="0.0">
                                          <val value="0.0">
                     </av>
                     <ic name=new type=ic subtype=observe>
                        <av name=repeatCount descr="No Description">
                                                <val value="1">
                        </av>
                     </ic>
                  </ic>
               </ic>
            </if>
         </ob>
         <ob name=new type=ob subtype=none>
            <av name=title descr="No Description">
                        <val value="standard star">
            </av>
            <av name=standard descr="No Description">
                        <val value="true">
            </av>
            <av name=chainedToPrev descr="No Description">
                        <val value="false">
            </av>
            <av name=chainedToNext descr="No Description">
                        <val value="false">
            </av>
            <av name=.gui.collapsed descr="No Description">
                        <val value="true">
            </av>
            <no name=new type=no subtype=none>
               <av name=title descr="No Description">
                              <val value="useage">
               </av>
               <av name=note descr="No Description">
                              <val value="This observation is flagged as a standard star. ORAC-DR uses this information to identify standards.  \n\nIf you want to turn the flag off move the yellow highlight to the \"standard star\" line above and click on the \"flag as standard\" box.">
               </av>
            </no>
            <no name=new type=no subtype=none>
               <av name=title descr="No Description">
                              <val value="standard coordinates information">
               </av>
               <av name=note descr="No Description">
                              <val value="enter your standard star (or target) co-ordinates into the target list below.  Note that although this is called a \"target list\" it only allows you to specify ONE astronomical target\n\nWhen you have done so you can use the \"position editor\" to  select a guide star, to fine tune the co-ordinate based on a sky survey picture, examine offset locations on the field etc.\n\nFor a step by step guide to how to use the position editor use \"help\".\n\nIF YOU WANT TO SELECT YOUR STANDARD STAR BY HAVING THE TSS SEARCH WITH THE TELESCOPE \"NEARSTAR\" IN REAL TIME THEN ENTER YOUR TARGET CO-ORDINATES HERE, AND DO NOT SPECIFY A GUIDE STAR.  AFTER THESE ARE SENT TO THE TELESCOPE THERE WILL BE\nA PAUSE FOR THE TSS TO SLEW TO THE CHOSEN NEARBY STAR.\n">
               </av>
            </no>
            <oc name=new type=oc subtype=targetList>
               <av name=Base descr="No Description">
                              <val value="Base">
                              <val value="enter your object name">
                              <val value="0:00:00">
                              <val value="0:00:00">
                              <val value="FK5 (J2000)">
               </av>
               <av name=.unique descr="No Description">
                              <val value="true">
               </av>
               <av name=.gui.selectedTelescopePos descr="No Description">
                              <val value="Base">
               </av>
            </oc>
            <no name=new type=no subtype=none>
               <av name=title descr="No Description">
                              <val value="cgs4 setup">
               </av>
               <av name=note descr="No Description">
                              <val value="For detailed advice about CGS4 see the web page at\nhttp://www.jach.hawaii.edu/JACpublic/UKIRT/instruments/cgs4/handbook.html\n\nEnter your desired CGS4 setup into the component below.  Make sure you choose the same settings for grating, wavelength, order, slit width and position angle for your STANDARD STAR as for your sources.\n\nNote that if you enter a central wavelength and choose a grating the order will be automatically\nselected. You may over-ride this if you wish, but seek advice on non-optimum orders from your support scientist.\n\nThere is feedback showing what the wavelength range and spectral resolution with the 1-pixel wide slit would be.\n\nEnter the slit width in pixels that you want to use.  Slit widths wider than 1-pixel may result in lower spectral resolution, depending on the seeing.  \n\nSlit position angles on the sky should fall in \nthe range 7 to -173 degrees EAST of NORTH\n\nIf you select a source magnitude range the tool will enter a recommended exposure time and number of coadds at each sampling position.  You may enter your own value instead if you prefer. If the exposure time is short (less than 1sec) consider using STARE mode instead.\n\nSampling is specified as \"Oversampling factor\" x\n\"number of pixels\" the spectrum is stepped over. So 2 x 2 sampling is Nyquist sampling stepped over 2 pixels giving a total of 4 independant measurements of the spectrum, 2 at each data point in the final spectrum.\n\n ">
               </av>
            </no>
            <oc name=new type=oc subtype=inst.CGS4>
               <av name=order descr="No Description">
                              <val value="1">
               </av>
               <av name=posAngle descr="No Description">
                              <val value="0.0">
               </av>
               <av name=filter descr="No Description">
                              <val value="B2">
               </av>
               <av name=coadds descr="No Description">
                              <val value="1">
               </av>
               <av name=polariser descr="No Description">
                              <val value="none">
               </av>
               <av name=acqMode descr="No Description">
                              <val value="NDSTARE">
               </av>
               <av name=disperser descr="No Description">
                              <val value="40lpmm">
               </av>
               <av name=.version descr="No Description">
                              <val value="1.0">
               </av>
               <av name=sourceMag descr="No Description">
                              <val value="7-8">
               </av>
               <av name=.unique descr="No Description">
                              <val value="true">
               </av>
               <av name=exposureTime descr="No Description">
                              <val value="7.0">
               </av>
               <av name=mask descr="No Description">
                              <val value="1pixel">
               </av>
               <av name=sampling descr="No Description">
                              <val value="2x2">
               </av>
               <av name=cvfOffset descr="No Description">
                              <val value="0.0">
               </av>
               <av name=instPort descr="No Description">
                              <val value="South">
               </av>
               <av name=centralWavelength descr="No Description">
                              <val value="2.1">
               </av>
               <av name=instAper descr="No Description">
                              <val value="-0.55">
                              <val value="7.62">
                              <val value="0.0">
                              <val value="2.1">
               </av>
               <av name=neutralDensity descr="No Description">
                              <val value="false">
               </av>
            </oc>
            <no name=new type=no subtype=none>
               <av name=title descr="No Description">
                              <val value="DR Recipe Selection">
               </av>
               <av name=note descr="No Description">
                              <val value="The data reduction recipe specified below uses the most basic data reduction recipe for speed.  (this is a good idea if your exposure time short).\n\n\n">
               </av>
            </no>
            <oc name=new type=oc subtype=DRRecipe>
               <av name=ObjectRecipe descr="No Description">
                              <val value="STANDARD_STAR">
               </av>
               <av name=DRRecipe descr="No Description">
                              <val value="JITTER5_SELF_FLAT">
               </av>
               <av name=DarkRecipe descr="No Description">
                              <val value="REDUCE_DARK">
               </av>
               <av name=SkyInGroup descr="No Description">
                              <val value="true">
               </av>
               <av name=.unique descr="No Description">
                              <val value="true">
               </av>
               <av name=ObjectInGroup descr="No Description">
                              <val value="true">
               </av>
               <av name=FlatRecipe descr="No Description">
                              <val value="REDUCE_FLAT">
               </av>
               <av name=DarkInGroup descr="No Description">
                              <val value="false">
               </av>
               <av name=SkyRecipe descr="No Description">
                              <val value="REDUCE_SKY">
               </av>
               <av name=ArcRecipe descr="No Description">
                              <val value="REDUCE_ARC">
               </av>
               <av name=BiasInGroup descr="No Description">
                              <val value="false">
               </av>
               <av name=title descr="No Description">
                              <val value="STANDARD_STAR">
               </av>
               <av name=FlatInGroup descr="No Description">
                              <val value="false">
               </av>
               <av name=BiasRecipe descr="No Description">
                              <val value="REDUCE_BIAS">
               </av>
               <av name=ArcInGroup descr="No Description">
                              <val value="false">
               </av>
            </oc>
            <if name=new type=if subtype=none>
               <av name=.gui.collapsed descr="No Description">
                              <val value="false">
               </av>
               <no name=new type=no subtype=none>
                  <av name=title descr="No Description">
                                    <val value="set the number of repeats">
                  </av>
                  <av name=note descr="No Description">
                                    <val value="To repeat the quads more than once on your standard star , use the repeat item below to specify how many times you want to repeat it.\n\n">
                  </av>
               </no>
               <ic name=new type=ic subtype=repeat>
                  <av name=repeatCount descr="No Description">
                                    <val value="2">
                  </av>
                  <av name=title descr="No Description">
                                    <val value="Repeat 2X">
                  </av>
                  <av name=.gui.collapsed descr="No Description">
                                    <val value="false">
                  </av>
                  <no name=new type=no subtype=none>
                     <av name=title descr="No Description">
                                          <val value="offset info">
                     </av>
                     <av name=note descr="No Description">
                                          <val value="this nods 30 rows along the slit\n\nOffsets are enetered in the co-ordinate frame of the slit\n\np = along the slit\nq = perpendicular to the slit">
                     </av>
                  </no>
                  <ic name=new type=ic subtype=offset>
                     <av name=.gui.selectedOffsetPos descr="No Description">
                                          <val value="Offset2">
                     </av>
                     <av name=Offset3 descr="No Description">
                                          <val value="0.0">
                                          <val value="0.0">
                     </av>
                     <av name=Offset2 descr="No Description">
                                          <val value="0.0">
                                          <val value="11.74">
                     </av>
                     <av name=Offset1 descr="No Description">
                                          <val value="0.0">
                                          <val value="11.74">
                     </av>
                     <av name=offsetPositions descr="No Description">
                                          <val value="Offset0">
                                          <val value="Offset1">
                                          <val value="Offset2">
                                          <val value="Offset3">
                     </av>
                     <av name=Offset0 descr="No Description">
                                          <val value="0.0">
                                          <val value="0.0">
                     </av>
                     <ic name=new type=ic subtype=observe>
                        <av name=repeatCount descr="No Description">
                                                <val value="1">
                        </av>
                     </ic>
                  </ic>
               </ic>
            </if>
         </ob>
         <ob name=new type=ob subtype=none>
            <av name=title descr="No Description">
                        <val value="flat,arc,target nod along slit">
            </av>
            <av name=standard descr="No Description">
                        <val value="false">
            </av>
            <av name=chainedToPrev descr="No Description">
                        <val value="false">
            </av>
            <av name=chainedToNext descr="No Description">
                        <val value="false">
            </av>
            <av name=.gui.collapsed descr="No Description">
                        <val value="true">
            </av>
            <no name=new type=no subtype=none>
               <av name=title descr="No Description">
                              <val value="useage">
               </av>
               <av name=note descr="No Description">
                              <val value="This observation does a flat and an arc as well as\nyour target.  \n\nIf you dont want to do the flat and arc you can\nskip over them when the sequence is running,\n\nAlternatively you can remove them from the sequence, or chose to start from the library sequence that is called target,nod along slit">
               </av>
            </no>
            <no name=new type=no subtype=none>
               <av name=title descr="No Description">
                              <val value="target coord. entry">
               </av>
               <av name=note descr="No Description">
                              <val value="enter your target co-ordinates into the target list below.  Note that although this is called a \"target list\" it only allows you to specify ONE astronomical target\n\nWhen you have done so you can use the \"position editor\" to  select a guide star, to fine tune the co-ordinate based on a sky survey picture, examine offset locations on the field etc.\n\nFor a step by step guide to how to use the position editor use \"help\".\n\n">
               </av>
            </no>
            <oc name=new type=oc subtype=targetList>
               <av name=Base descr="No Description">
                              <val value="Base">
                              <val value="enter your object name">
                              <val value="0:00:00">
                              <val value="0:00:00">
                              <val value="FK5 (J2000)">
               </av>
               <av name=.unique descr="No Description">
                              <val value="true">
               </av>
               <av name=.gui.selectedTelescopePos descr="No Description">
                              <val value="Base">
               </av>
            </oc>
            <no name=new type=no subtype=none>
               <av name=title descr="No Description">
                              <val value="cgs4 setup">
               </av>
               <av name=note descr="No Description">
                              <val value="For detailed advice about CGS4 see the web page at\nhttp://www.jach.hawaii.edu/JACpublic/UKIRT/instruments/cgs4/handbook.html\n\nEnter your desired CGS4 setup into the component below.  Make sure you choose the same settings for grating, wavelength, order, slit width and position angle for your STANDARD STAR as for your sources.\n\nNote that if you enter a central wavelength and choose a grating the order will be automatically\nselected. You may over-ride this if you wish, but seek advice on non-optimum orders from your support scientist.\n\nThere is feedback showing what the wavelength range and spectral resolution with the 1-pixel wide slit would be.\n\nEnter the slit width in pixels that you want to use.  Slit widths wider than 1-pixel may result in lower spectral resolution, depending on the seeing.  \n\nSlit position angles on the sky should fall in \nthe range 7 to -173 degrees EAST of NORTH\n\nIf you select a source magnitude range the tool will enter a recommended exposure time and number of coadds at each sampling position.  You may enter your own value instead if you prefer. If the exposure time is short (less than 1sec) consider using STARE mode instead.\n\nSampling is specified as \"Oversampling factor\" x\n\"number of pixels\" the spectrum is stepped over. So 2 x 2 sampling is Nyquist sampling stepped over 2 pixels giving a total of 4 independant measurements of the spectrum, 2 at each data point in the final spectrum.\n\n ">
               </av>
            </no>
            <oc name=new type=oc subtype=inst.CGS4>
               <av name=order descr="No Description">
                              <val value="1">
               </av>
               <av name=posAngle descr="No Description">
                              <val value="0.0">
               </av>
               <av name=filter descr="No Description">
                              <val value="B2">
               </av>
               <av name=coadds descr="No Description">
                              <val value="1">
               </av>
               <av name=polariser descr="No Description">
                              <val value="none">
               </av>
               <av name=acqMode descr="No Description">
                              <val value="NDSTARE">
               </av>
               <av name=disperser descr="No Description">
                              <val value="40lpmm">
               </av>
               <av name=.version descr="No Description">
                              <val value="1.0">
               </av>
               <av name=sourceMag descr="No Description">
                              <val value="7-8">
               </av>
               <av name=.unique descr="No Description">
                              <val value="true">
               </av>
               <av name=exposureTime descr="No Description">
                              <val value="14.0">
               </av>
               <av name=mask descr="No Description">
                              <val value="1pixel">
               </av>
               <av name=sampling descr="No Description">
                              <val value="2x2">
               </av>
               <av name=cvfOffset descr="No Description">
                              <val value="0.0">
               </av>
               <av name=instPort descr="No Description">
                              <val value="South">
               </av>
               <av name=centralWavelength descr="No Description">
                              <val value="2.1">
               </av>
               <av name=instAper descr="No Description">
                              <val value="-0.55">
                              <val value="7.62">
                              <val value="0.0">
                              <val value="2.1">
               </av>
               <av name=neutralDensity descr="No Description">
                              <val value="0">
               </av>
            </oc>
            <no name=new type=no subtype=none>
               <av name=title descr="No Description">
                              <val value="DR Recipe Selection">
               </av>
               <av name=note descr="No Description">
                              <val value="The data reduction recipe specified below uses the most basic data reduction recipe.\n\nOthers suitable for \"quads\" type data are listed in the DRrecipe component below.\n\n\n">
               </av>
            </no>
            <oc name=new type=oc subtype=DRRecipe>
               <av name=ObjectRecipe descr="No Description">
                              <val value="SOURCE_PAIRS_ON_SLIT">
               </av>
               <av name=DRRecipe descr="No Description">
                              <val value="JITTER5_SELF_FLAT">
               </av>
               <av name=DarkRecipe descr="No Description">
                              <val value="REDUCE_DARK">
               </av>
               <av name=SkyInGroup descr="No Description">
                              <val value="false">
               </av>
               <av name=.unique descr="No Description">
                              <val value="true">
               </av>
               <av name=ObjectInGroup descr="No Description">
                              <val value="true">
               </av>
               <av name=FlatRecipe descr="No Description">
                              <val value="REDUCE_FLAT">
               </av>
               <av name=DarkInGroup descr="No Description">
                              <val value="false">
               </av>
               <av name=SkyRecipe descr="No Description">
                              <val value="REDUCE_SKY">
               </av>
               <av name=ArcRecipe descr="No Description">
                              <val value="REDUCE_ARC">
               </av>
               <av name=BiasInGroup descr="No Description">
                              <val value="false">
               </av>
               <av name=title descr="No Description">
                              <val value="SOURCE_PAIRS_ON_SLIT">
               </av>
               <av name=FlatInGroup descr="No Description">
                              <val value="false">
               </av>
               <av name=BiasRecipe descr="No Description">
                              <val value="REDUCE_BIAS">
               </av>
               <av name=ArcInGroup descr="No Description">
                              <val value="false">
               </av>
            </oc>
            <if name=new type=if subtype=none>
               <av name=.gui.collapsed descr="No Description">
                              <val value="false">
               </av>
               <no name=new type=no subtype=none>
                  <av name=title descr="No Description">
                                    <val value="flat - how to use">
                  </av>
                  <av name=note descr="No Description">
                                    <val value="CLICK ON THE \"DEFAULT\" BUTTON \n\nto get sensible default values for the flat lamp and its aperture, the order sorting filter and the exposure time.  These may all be changed if you wish.  To get the default values back, click on default again.\n\nTo get a single higher signal to noise flat increase the number of coadds\n\nTo take several flats for later addition increase the number next to \"observe\"">
                  </av>
               </no>
               <ic name=new type=ic subtype=CGS4calUnitObs>
                  <av name=flatSampling descr="No Description">
                                    <val value="1x1">
                  </av>
                  <av name=filter descr="No Description">
                                    <val value="B2">
                  </av>
                  <av name=coadds descr="No Description">
                                    <val value="5">
                  </av>
                  <av name=calType descr="No Description">
                                    <val value="Flat">
                  </av>
                  <av name=repeatCount descr="No Description">
                                    <val value="1">
                  </av>
                  <av name=exposureTime descr="No Description">
                                    <val value="">
                  </av>
                  <av name=cvfWavelength descr="No Description">
                                    <val value="0.0">
                  </av>
                  <av name=acqMode descr="No Description">
                                    <val value="NDSTARE">
                  </av>
                  <av name=lamp descr="No Description">
                                    <val value="Black Body (1.3)">
                  </av>
               </ic>
               <no name=new type=no subtype=none>
                  <av name=title descr="No Description">
                                    <val value="arc - how to use">
                  </av>
                  <av name=note descr="No Description">
                                    <val value="CLICK ON THE \"DEFAULT\" BUTTON \n\nto get sensible default values for the arc lamp, the order sorting filter and the exposure time.  These may all be changed if you wish.  To get the default values back, click on default again.\n\nNote that for wavelengths longer than 3.?um the default blocking filter is set so that arcs are obtained in a x 2 higher order.  (the glass of the\nlamps does not transmit thermal lines).\n\nTo get a single higher signal to noise arc increase the number of coadds\n\nTo take several arcs for later addition increase the number next to \"observe\"\n\nto do two or more arcs using different arc lamps :\nMove the yellow highlight to the observe eye labelled Arc below. Under \"edit\" at the top of the component click on \"copy\". Then click on \"paste\" to add extra arc observations as many times as you need.  Go into each arc component - click on default to get a sensible order sorting filter, then select a different arc lamp or other item.\n">
                  </av>
               </no>
               <ic name=new type=ic subtype=CGS4calUnitObs>
                  <av name=flatSampling descr="No Description">
                                    <val value="1x1">
                  </av>
                  <av name=filter descr="No Description">
                                    <val value="B2">
                  </av>
                  <av name=coadds descr="No Description">
                                    <val value="5">
                  </av>
                  <av name=calType descr="No Description">
                                    <val value="Arc">
                  </av>
                  <av name=repeatCount descr="No Description">
                                    <val value="1">
                  </av>
                  <av name=exposureTime descr="No Description">
                                    <val value="">
                  </av>
                  <av name=cvfWavelength descr="No Description">
                                    <val value="0.0">
                  </av>
                  <av name=acqMode descr="No Description">
                                    <val value="NDSTARE">
                  </av>
                  <av name=lamp descr="No Description">
                                    <val value="Argon Arc">
                  </av>
               </ic>
               <no name=new type=no subtype=none>
                  <av name=title descr="No Description">
                                    <val value="set the number of repeats">
                  </av>
                  <av name=note descr="No Description">
                                    <val value="To repeat the quads more than once on your target , use the repeat item below to specify how many times you want to repeat it.\n\n">
                  </av>
               </no>
               <ic name=new type=ic subtype=repeat>
                  <av name=repeatCount descr="No Description">
                                    <val value="20">
                  </av>
                  <av name=title descr="No Description">
                                    <val value="Repeat 20X">
                  </av>
                  <av name=.gui.collapsed descr="No Description">
                                    <val value="false">
                  </av>
                  <no name=new type=no subtype=none>
                     <av name=title descr="No Description">
                                          <val value="offset info">
                     </av>
                     <av name=note descr="No Description">
                                          <val value="this nods 30 rows along the slit\n\nNote that offsets are entered here in the co-ordinate frame of the slit.">
                     </av>
                  </no>
                  <ic name=new type=ic subtype=offset>
                     <av name=.gui.selectedOffsetPos descr="No Description">
                                          <val value="Offset2">
                     </av>
                     <av name=Offset3 descr="No Description">
                                          <val value="0.0">
                                          <val value="0.0">
                     </av>
                     <av name=Offset2 descr="No Description">
                                          <val value="0.0">
                                          <val value="11.74">
                     </av>
                     <av name=Offset1 descr="No Description">
                                          <val value="0.0">
                                          <val value="11.74">
                     </av>
                     <av name=offsetPositions descr="No Description">
                                          <val value="Offset0">
                                          <val value="Offset1">
                                          <val value="Offset2">
                                          <val value="Offset3">
                     </av>
                     <av name=Offset0 descr="No Description">
                                          <val value="0.0">
                                          <val value="0.0">
                     </av>
                     <ic name=new type=ic subtype=observe>
                        <av name=repeatCount descr="No Description">
                                                <val value="1">
                        </av>
                     </ic>
                  </ic>
               </ic>
            </if>
         </ob>
         <ob name=new type=ob subtype=none>
            <av name=title descr="No Description">
                        <val value="flat,arc,target, nod to blank sky">
            </av>
            <av name=standard descr="No Description">
                        <val value="false">
            </av>
            <av name=chainedToPrev descr="No Description">
                        <val value="false">
            </av>
            <av name=chainedToNext descr="No Description">
                        <val value="false">
            </av>
            <av name=.gui.collapsed descr="No Description">
                        <val value="true">
            </av>
            <no name=new type=no subtype=none>
               <av name=title descr="No Description">
                              <val value="target coord. entry">
               </av>
               <av name=note descr="No Description">
                              <val value="enter your target co-ordinates into the target list below.  Note that although this is called a \"target list\" it only allows you to specify ONE astronomical target\n\nWhen you have done so you can use the \"position editor\" to  select a guide star, to fine tune the co-ordinate based on a sky survey picture, examine offset locations on the field etc.\n\nFor a step by step guide to how to use the position editor use \"help\".\n\n">
               </av>
            </no>
            <oc name=new type=oc subtype=targetList>
               <av name=Base descr="No Description">
                              <val value="Base">
                              <val value="enter your object name">
                              <val value="0:00:00">
                              <val value="0:00:00">
                              <val value="FK5 (J2000)">
               </av>
               <av name=.unique descr="No Description">
                              <val value="true">
               </av>
               <av name=.gui.selectedTelescopePos descr="No Description">
                              <val value="Base">
               </av>
            </oc>
            <no name=new type=no subtype=none>
               <av name=title descr="No Description">
                              <val value="cgs4 setup">
               </av>
               <av name=note descr="No Description">
                              <val value="For detailed advice about CGS4 see the web page at\nhttp://www.jach.hawaii.edu/JACpublic/UKIRT/instruments/cgs4/handbook.html\n\nEnter your desired CGS4 setup into the component below.  Make sure you choose the same settings for grating, wavelength, order, slit width and position angle for your STANDARD STAR as for your sources.\n\nNote that if you enter a central wavelength and choose a grating the order will be automatically\nselected. You may over-ride this if you wish, but seek advice on non-optimum orders from your support scientist.\n\nThere is feedback showing what the wavelength range and spectral resolution with the 1-pixel wide slit would be.\n\nEnter the slit width in pixels that you want to use.  Slit widths wider than 1-pixel may result in lower spectral resolution, depending on the seeing.  \n\nSlit position angles on the sky should fall in \nthe range 7 to -173 degrees EAST of NORTH\n\nIf you select a source magnitude range the tool will enter a recommended exposure time and number of coadds at each sampling position.  You may enter your own value instead if you prefer. If the exposure time is short (less than 1sec) consider using STARE mode instead.\n\nSampling is specified as \"Oversampling factor\" x\n\"number of pixels\" the spectrum is stepped over. So 2 x 2 sampling is Nyquist sampling stepped over 2 pixels giving a total of 4 independant measurements of the spectrum, 2 at each data point in the final spectrum.\n\n ">
               </av>
            </no>
            <oc name=new type=oc subtype=inst.CGS4>
               <av name=order descr="No Description">
                              <val value="1">
               </av>
               <av name=posAngle descr="No Description">
                              <val value="0">
               </av>
               <av name=filter descr="No Description">
                              <val value="B2">
               </av>
               <av name=coadds descr="No Description">
                              <val value="1">
               </av>
               <av name=polariser descr="No Description">
                              <val value="none">
               </av>
               <av name=acqMode descr="No Description">
                              <val value="NDSTARE">
               </av>
               <av name=disperser descr="No Description">
                              <val value="40lpmm">
               </av>
               <av name=.version descr="No Description">
                              <val value="1.0">
               </av>
               <av name=sourceMag descr="No Description">
                              <val value="7-8">
               </av>
               <av name=.unique descr="No Description">
                              <val value="true">
               </av>
               <av name=exposureTime descr="No Description">
                              <val value="14.0">
               </av>
               <av name=mask descr="No Description">
                              <val value="1pixel">
               </av>
               <av name=sampling descr="No Description">
                              <val value="2x2">
               </av>
               <av name=cvfOffset descr="No Description">
                              <val value="0.0">
               </av>
               <av name=instPort descr="No Description">
                              <val value="South">
               </av>
               <av name=centralWavelength descr="No Description">
                              <val value="2.1">
               </av>
               <av name=instAper descr="No Description">
                              <val value="-0.55">
                              <val value="7.62">
                              <val value="0.0">
                              <val value="2.1">
               </av>
               <av name=neutralDensity descr="No Description">
                              <val value="0">
               </av>
            </oc>
            <no name=new type=no subtype=none>
               <av name=title descr="No Description">
                              <val value="DR Recipe Selection">
               </av>
               <av name=note descr="No Description">
                              <val value="The data reduction recipe specified below uses the most basic data reduction recipe.\n\nOthers suitable for \"quads\" type data are listed in the DRrecipe component below.\n\nAlthough this is a nod to sky type observation it is done in a \"quads pattern\" : obj, sky, sky, obj, and so can be reduced using the standard quads recipe.  \n\nOther reduction recipes will soon be available for nodding to sky less frequently than in quads.\n">
               </av>
            </no>
            <oc name=new type=oc subtype=DRRecipe>
               <av name=ObjectRecipe descr="No Description">
                              <val value="SOURCE_WITH_NOD_TO_BLANK_SKY">
               </av>
               <av name=DRRecipe descr="No Description">
                              <val value="JITTER5_SELF_FLAT">
               </av>
               <av name=DarkRecipe descr="No Description">
                              <val value="REDUCE_DARK">
               </av>
               <av name=SkyInGroup descr="No Description">
                              <val value="false">
               </av>
               <av name=.unique descr="No Description">
                              <val value="true">
               </av>
               <av name=ObjectInGroup descr="No Description">
                              <val value="true">
               </av>
               <av name=FlatRecipe descr="No Description">
                              <val value="REDUCE_FLAT">
               </av>
               <av name=DarkInGroup descr="No Description">
                              <val value="false">
               </av>
               <av name=SkyRecipe descr="No Description">
                              <val value="REDUCE_SKY">
               </av>
               <av name=ArcRecipe descr="No Description">
                              <val value="REDUCE_ARC">
               </av>
               <av name=BiasInGroup descr="No Description">
                              <val value="false">
               </av>
               <av name=title descr="No Description">
                              <val value="SOURCE_WITH_NOD_TO_BLANK_SKY">
               </av>
               <av name=FlatInGroup descr="No Description">
                              <val value="false">
               </av>
               <av name=BiasRecipe descr="No Description">
                              <val value="REDUCE_BIAS">
               </av>
               <av name=ArcInGroup descr="No Description">
                              <val value="false">
               </av>
            </oc>
            <if name=new type=if subtype=none>
               <av name=.gui.collapsed descr="No Description">
                              <val value="false">
               </av>
               <no name=new type=no subtype=none>
                  <av name=title descr="No Description">
                                    <val value="flat - how to use">
                  </av>
                  <av name=note descr="No Description">
                                    <val value="CLICK ON THE \"DEFAULT\" BUTTON \n\nto get sensible default values for the flat lamp and its aperture, the order sorting filter and the exposure time.  These may all be changed if you wish.  To get the default values back, click on default again.\n\nTo get a single higher signal to noise flat increase the number of coadds\n\nTo take several flats for later addition increase the number next to \"observe\"">
                  </av>
               </no>
               <ic name=new type=ic subtype=CGS4calUnitObs>
                  <av name=flatSampling descr="No Description">
                                    <val value="1x1">
                  </av>
                  <av name=filter descr="No Description">
                                    <val value="B2">
                  </av>
                  <av name=coadds descr="No Description">
                                    <val value="5">
                  </av>
                  <av name=calType descr="No Description">
                                    <val value="Flat">
                  </av>
                  <av name=repeatCount descr="No Description">
                                    <val value="1">
                  </av>
                  <av name=exposureTime descr="No Description">
                                    <val value="">
                  </av>
                  <av name=cvfWavelength descr="No Description">
                                    <val value="0.0">
                  </av>
                  <av name=acqMode descr="No Description">
                                    <val value="NDSTARE">
                  </av>
                  <av name=lamp descr="No Description">
                                    <val value="Black Body (1.3)">
                  </av>
               </ic>
               <no name=new type=no subtype=none>
                  <av name=title descr="No Description">
                                    <val value="arc - how to use">
                  </av>
                  <av name=note descr="No Description">
                                    <val value="CLICK ON THE \"DEFAULT\" BUTTON \n\nto get sensible default values for the arc lamp, the order sorting filter and the exposure time.  These may all be changed if you wish.  To get the default values back, click on default again.\n\nNote that for wavelengths longer than 3.?um the default blocking filter is set so that arcs are obtained in a x 2 higher order.  (the glass of the\nlamps does not transmit thermal lines).\n\nTo get a single higher signal to noise arc increase the number of coadds\n\nTo take several arcs for later addition increase the number next to \"observe\"\n\nto do two or more arcs using different arc lamps :\nMove the yellow highlight to the observe eye labelled Arc below. Under \"edit\" at the top of the component click on \"copy\". Then click on \"paste\" to add extra arc observations as many times as you need.  Go into each arc component - click on default to get a sensible order sorting filter, then select a different arc lamp or other item.\n">
                  </av>
               </no>
               <ic name=new type=ic subtype=CGS4calUnitObs>
                  <av name=flatSampling descr="No Description">
                                    <val value="1x1">
                  </av>
                  <av name=filter descr="No Description">
                                    <val value="B2">
                  </av>
                  <av name=coadds descr="No Description">
                                    <val value="5">
                  </av>
                  <av name=calType descr="No Description">
                                    <val value="Arc">
                  </av>
                  <av name=repeatCount descr="No Description">
                                    <val value="1">
                  </av>
                  <av name=exposureTime descr="No Description">
                                    <val value="">
                  </av>
                  <av name=cvfWavelength descr="No Description">
                                    <val value="0.0">
                  </av>
                  <av name=acqMode descr="No Description">
                                    <val value="NDSTARE">
                  </av>
                  <av name=lamp descr="No Description">
                                    <val value="Argon Arc">
                  </av>
               </ic>
               <no name=new type=no subtype=none>
                  <av name=title descr="No Description">
                                    <val value="set the number of repeats">
                  </av>
                  <av name=note descr="No Description">
                                    <val value="To repeat the quads more than once on your target , use the repeat item below to specify how many times you want to repeat it.\n\n">
                  </av>
               </no>
               <ic name=new type=ic subtype=repeat>
                  <av name=repeatCount descr="No Description">
                                    <val value="20">
                  </av>
                  <av name=title descr="No Description">
                                    <val value="Repeat 20X">
                  </av>
                  <av name=.gui.collapsed descr="No Description">
                                    <val value="false">
                  </av>
                  <no name=new type=no subtype=none>
                     <av name=title descr="No Description">
                                          <val value="offset info">
                     </av>
                     <av name=note descr="No Description">
                                          <val value="this offsets 60 arc sec perpendicular to the slit to take you to blank sky.\n\nThe sky obsrvations are specified as sky by the \"sky observe eye\" instead of the \"observe eye\" this ensures that blank sky frames are properly labelled in their headers.\n\nIf you want to offset further, edit the offset value for p in the 2nd and third offset items below.  \n\nNote that offsets are entered in the co-ordinate frame of THE SLIT.\n\n">
                     </av>
                  </no>
                  <ic name=new type=ic subtype=offset>
                     <av name=offsetPositions descr="No Description">
                                          <val value="Offset0">
                     </av>
                     <av name=Offset0 descr="No Description">
                                          <val value="0.0">
                                          <val value="0.0">
                     </av>
                     <av name=.gui.selectedOffsetPos descr="No Description">
                                          <val value="Offset0">
                     </av>
                     <ic name=new type=ic subtype=observe>
                        <av name=repeatCount descr="No Description">
                                                <val value="1">
                        </av>
                     </ic>
                  </ic>
                  <ic name=new type=ic subtype=offset>
                     <av name=.gui.selectedOffsetPos descr="No Description">
                                          <val value="Offset0">
                     </av>
                     <av name=Offset0 descr="No Description">
                                          <val value="60.0">
                                          <val value="0.0">
                     </av>
                     <av name=offsetPositions descr="No Description">
                                          <val value="Offset0">
                     </av>
                     <av name=.gui.collapsed descr="No Description">
                                          <val value="false">
                     </av>
                     <ic name=new type=ic subtype=sky>
                        <av name=repeatCount descr="No Description">
                                                <val value="2">
                        </av>
                     </ic>
                  </ic>
                  <ic name=new type=ic subtype=offset>
                     <av name=.gui.selectedOffsetPos descr="No Description">
                                          <val value="Offset0">
                     </av>
                     <av name=Offset0 descr="No Description">
                                          <val value="0.0">
                                          <val value="0.0">
                     </av>
                     <av name=offsetPositions descr="No Description">
                                          <val value="Offset0">
                     </av>
                     <av name=.gui.collapsed descr="No Description">
                                          <val value="false">
                     </av>
                     <ic name=new type=ic subtype=observe>
                        <av name=repeatCount descr="No Description">
                                                <val value="1">
                        </av>
                     </ic>
                  </ic>
               </ic>
            </if>
         </ob>
         <ob name=new type=ob subtype=none>
            <av name=title descr="No Description">
                        <val value="target, nod along slit">
            </av>
            <av name=standard descr="No Description">
                        <val value="false">
            </av>
            <av name=chainedToPrev descr="No Description">
                        <val value="false">
            </av>
            <av name=chainedToNext descr="No Description">
                        <val value="false">
            </av>
            <av name=.gui.collapsed descr="No Description">
                        <val value="true">
            </av>
            <no name=new type=no subtype=none>
               <av name=title descr="No Description">
                              <val value="target information">
               </av>
               <av name=note descr="No Description">
                              <val value="enter your target co-ordinates into the target list below.  Note that although this is called a \"target list\" it only allows you to specify ONE astronomical target\n\nWhen you have done so you can use the \"position editor\" to  select a guide star, to fine tune the co-ordinate based on a sky survey picture, examine offset locations on the field etc.\n\nFor a step by step guide to how to use the position editor use \"help\".">
               </av>
            </no>
            <oc name=new type=oc subtype=targetList>
               <av name=Base descr="No Description">
                              <val value="Base">
                              <val value="enter your object name">
                              <val value="0:00:00">
                              <val value="0:00:00">
                              <val value="FK5 (J2000)">
               </av>
               <av name=.unique descr="No Description">
                              <val value="true">
               </av>
               <av name=.gui.selectedTelescopePos descr="No Description">
                              <val value="Base">
               </av>
            </oc>
            <no name=new type=no subtype=none>
               <av name=title descr="No Description">
                              <val value="cgs4 setup">
               </av>
               <av name=note descr="No Description">
                              <val value="For detailed advice about CGS4 see the web page at\nhttp://www.jach.hawaii.edu/JACpublic/UKIRT/instruments/cgs4/handbook.html\n\nEnter your desired CGS4 setup into the component below.  Make sure you choose the same settings for grating, wavelength, order, slit width and position angle for your arcs and flats as for your sources.\n\nNote that if you enter a central wavelength and choose a grating the order will be automatically\nselected. You may over-ride this if you wish, but seek advice on non-optimum orders from your support scientist.\n\nThere is feedback showing what the wavelength range and spectral resolution with the 1-pixel wide slit would be.\n\nEnter the slit width in pixels that you want to use.  Slit widths wider than 1-pixel may result in lower spectral resolution, depending on the seeing.  \n\nSlit position angles on the sky should fall in \nthe range -7 to -173 degrees EAST of NORTH\n\nIf you select a source magnitude range the tool will enter a recommended exposure time and number of coadds at each sampling position.  You may enter your own value instead if you prefer. If the exposure time is short (less than 1sec) consider using STARE mode instead.\n\nSampling is specified as \"Oversampling factor\" x\n\"number of pixels\" the spectrum is stepped over. So 2 x 2 sampling is Nyquist sampling stepped over 2 pixels giving a total of 4 independant measurements of the spectrum, 2 at each data point in the final spectrum.\n\n ">
               </av>
            </no>
            <oc name=new type=oc subtype=inst.CGS4>
               <av name=order descr="No Description">
                              <val value="1">
               </av>
               <av name=posAngle descr="No Description">
                              <val value="0.0">
               </av>
               <av name=filter descr="No Description">
                              <val value="B2">
               </av>
               <av name=coadds descr="No Description">
                              <val value="2">
               </av>
               <av name=polariser descr="No Description">
                              <val value="none">
               </av>
               <av name=acqMode descr="No Description">
                              <val value="NDSTARE">
               </av>
               <av name=disperser descr="No Description">
                              <val value="40lpmm">
               </av>
               <av name=.version descr="No Description">
                              <val value="1.0">
               </av>
               <av name=sourceMag descr="No Description">
                              <val value="none">
               </av>
               <av name=.unique descr="No Description">
                              <val value="true">
               </av>
               <av name=exposureTime descr="No Description">
                              <val value="4.0">
               </av>
               <av name=mask descr="No Description">
                              <val value="1pixel">
               </av>
               <av name=sampling descr="No Description">
                              <val value="2x2">
               </av>
               <av name=cvfOffset descr="No Description">
                              <val value="0.0">
               </av>
               <av name=instPort descr="No Description">
                              <val value="South">
               </av>
               <av name=centralWavelength descr="No Description">
                              <val value="2.1">
               </av>
               <av name=instAper descr="No Description">
                              <val value="-0.55">
                              <val value="7.62">
                              <val value="0.0">
                              <val value="2.1">
               </av>
               <av name=neutralDensity descr="No Description">
                              <val value="false">
               </av>
            </oc>
            <no name=new type=no subtype=none>
               <av name=title descr="No Description">
                              <val value="DR Recipe Selection">
               </av>
               <av name=note descr="No Description">
                              <val value="The data reduction recipe specified below uses the most basic data reduction recipe for speed.  (this is a good idea if your exposure time short).\n\n">
               </av>
            </no>
            <oc name=new type=oc subtype=DRRecipe>
               <av name=ObjectRecipe descr="No Description">
                              <val value="SOURCE_PAIRS_ON_SLIT">
               </av>
               <av name=DRRecipe descr="No Description">
                              <val value="JITTER5_SELF_FLAT">
               </av>
               <av name=DarkRecipe descr="No Description">
                              <val value="REDUCE_DARK">
               </av>
               <av name=SkyInGroup descr="No Description">
                              <val value="false">
               </av>
               <av name=.unique descr="No Description">
                              <val value="true">
               </av>
               <av name=ObjectInGroup descr="No Description">
                              <val value="true">
               </av>
               <av name=FlatRecipe descr="No Description">
                              <val value="REDUCE_FLAT">
               </av>
               <av name=DarkInGroup descr="No Description">
                              <val value="false">
               </av>
               <av name=SkyRecipe descr="No Description">
                              <val value="REDUCE_SKY">
               </av>
               <av name=ArcRecipe descr="No Description">
                              <val value="REDUCE_ARC">
               </av>
               <av name=BiasInGroup descr="No Description">
                              <val value="false">
               </av>
               <av name=title descr="No Description">
                              <val value="SOURCE_PAIRS_ON_SLIT">
               </av>
               <av name=FlatInGroup descr="No Description">
                              <val value="false">
               </av>
               <av name=BiasRecipe descr="No Description">
                              <val value="REDUCE_BIAS">
               </av>
               <av name=ArcInGroup descr="No Description">
                              <val value="false">
               </av>
            </oc>
            <if name=new type=if subtype=none>
               <av name=.gui.collapsed descr="No Description">
                              <val value="false">
               </av>
               <no name=new type=no subtype=none>
                  <av name=title descr="No Description">
                                    <val value="how to do more than one 5-point jitters">
                  </av>
                  <av name=note descr="No Description">
                                    <val value="To repeat the quads more than once on your target , use the repeat item below to specify how many times you want to repeat it.\n\n">
                  </av>
               </no>
               <ic name=new type=ic subtype=repeat>
                  <av name=repeatCount descr="No Description">
                                    <val value="10">
                  </av>
                  <av name=title descr="No Description">
                                    <val value="Repeat 10X">
                  </av>
                  <av name=.gui.collapsed descr="No Description">
                                    <val value="false">
                  </av>
                  <no name=new type=no subtype=none>
                     <av name=title descr="No Description">
                                          <val value="offset info">
                     </av>
                     <av name=note descr="No Description">
                                          <val value="this nods 30 rows along the slit">
                     </av>
                  </no>
                  <ic name=new type=ic subtype=offset>
                     <av name=.gui.selectedOffsetPos descr="No Description">
                                          <val value="Offset2">
                     </av>
                     <av name=Offset3 descr="No Description">
                                          <val value="0.0">
                                          <val value="0.0">
                     </av>
                     <av name=Offset2 descr="No Description">
                                          <val value="0.0">
                                          <val value="11.74">
                     </av>
                     <av name=Offset1 descr="No Description">
                                          <val value="0.0">
                                          <val value="11.74">
                     </av>
                     <av name=offsetPositions descr="No Description">
                                          <val value="Offset0">
                                          <val value="Offset1">
                                          <val value="Offset2">
                                          <val value="Offset3">
                     </av>
                     <av name=Offset0 descr="No Description">
                                          <val value="0.0">
                                          <val value="0.0">
                     </av>
                     <ic name=new type=ic subtype=observe>
                        <av name=repeatCount descr="No Description">
                                                <val value="1">
                        </av>
                     </ic>
                  </ic>
               </ic>
            </if>
         </ob>
         <ob name=new type=ob subtype=none>
            <av name=title descr="No Description">
                        <val value="target, nod to blank sky">
            </av>
            <av name=standard descr="No Description">
                        <val value="false">
            </av>
            <av name=chainedToPrev descr="No Description">
                        <val value="false">
            </av>
            <av name=chainedToNext descr="No Description">
                        <val value="false">
            </av>
            <av name=.gui.collapsed descr="No Description">
                        <val value="true">
            </av>
            <no name=new type=no subtype=none>
               <av name=title descr="No Description">
                              <val value="target coord. entry">
               </av>
               <av name=note descr="No Description">
                              <val value="enter your target co-ordinates into the target list below.  Note that although this is called a \"target list\" it only allows you to specify ONE astronomical target\n\nWhen you have done so you can use the \"position editor\" to  select a guide star, to fine tune the co-ordinate based on a sky survey picture, examine offset locations on the field etc.\n\nFor a step by step guide to how to use the position editor use \"help\".\n\n">
               </av>
            </no>
            <oc name=new type=oc subtype=targetList>
               <av name=Base descr="No Description">
                              <val value="Base">
                              <val value="enter your object name">
                              <val value="0:00:00">
                              <val value="0:00:00">
                              <val value="FK5 (J2000)">
               </av>
               <av name=.unique descr="No Description">
                              <val value="true">
               </av>
               <av name=.gui.selectedTelescopePos descr="No Description">
                              <val value="Base">
               </av>
            </oc>
            <no name=new type=no subtype=none>
               <av name=title descr="No Description">
                              <val value="cgs4 setup">
               </av>
               <av name=note descr="No Description">
                              <val value="For detailed advice about CGS4 see the web page at\nhttp://www.jach.hawaii.edu/JACpublic/UKIRT/instruments/cgs4/handbook.html\n\nEnter your desired CGS4 setup into the component below.  Make sure you choose the same settings for grating, wavelength, order, slit width and position angle for your STANDARD STAR as for your sources.\n\nNote that if you enter a central wavelength and choose a grating the order will be automatically\nselected. You may over-ride this if you wish, but seek advice on non-optimum orders from your support scientist.\n\nThere is feedback showing what the wavelength range and spectral resolution with the 1-pixel wide slit would be.\n\nEnter the slit width in pixels that you want to use.  Slit widths wider than 1-pixel may result in lower spectral resolution, depending on the seeing.  \n\nSlit position angles on the sky should fall in \nthe range 7 to -173 degrees EAST of NORTH\n\nIf you select a source magnitude range the tool will enter a recommended exposure time and number of coadds at each sampling position.  You may enter your own value instead if you prefer. If the exposure time is short (less than 1sec) consider using STARE mode instead.\n\nSampling is specified as \"Oversampling factor\" x\n\"number of pixels\" the spectrum is stepped over. So 2 x 2 sampling is Nyquist sampling stepped over 2 pixels giving a total of 4 independant measurements of the spectrum, 2 at each data point in the final spectrum.\n\n ">
               </av>
            </no>
            <oc name=new type=oc subtype=inst.CGS4>
               <av name=order descr="No Description">
                              <val value="1">
               </av>
               <av name=posAngle descr="No Description">
                              <val value="0.0">
               </av>
               <av name=filter descr="No Description">
                              <val value="B2">
               </av>
               <av name=coadds descr="No Description">
                              <val value="1">
               </av>
               <av name=polariser descr="No Description">
                              <val value="none">
               </av>
               <av name=acqMode descr="No Description">
                              <val value="NDSTARE">
               </av>
               <av name=disperser descr="No Description">
                              <val value="40lpmm">
               </av>
               <av name=.version descr="No Description">
                              <val value="1.0">
               </av>
               <av name=sourceMag descr="No Description">
                              <val value="7-8">
               </av>
               <av name=.unique descr="No Description">
                              <val value="true">
               </av>
               <av name=exposureTime descr="No Description">
                              <val value="14.0">
               </av>
               <av name=mask descr="No Description">
                              <val value="1pixel">
               </av>
               <av name=sampling descr="No Description">
                              <val value="2x2">
               </av>
               <av name=cvfOffset descr="No Description">
                              <val value="0.0">
               </av>
               <av name=instPort descr="No Description">
                              <val value="South">
               </av>
               <av name=centralWavelength descr="No Description">
                              <val value="2.1">
               </av>
               <av name=instAper descr="No Description">
                              <val value="-0.55">
                              <val value="7.62">
                              <val value="0.0">
                              <val value="2.1">
               </av>
               <av name=neutralDensity descr="No Description">
                              <val value="0">
               </av>
            </oc>
            <no name=new type=no subtype=none>
               <av name=title descr="No Description">
                              <val value="DR Recipe Selection">
               </av>
               <av name=note descr="No Description">
                              <val value="The data reduction recipe specified below uses the most basic data reduction recipe.\n\nOthers suitable for \"quads\" type data are listed in the DRrecipe component below.\n\n\nAlthough this is a nod to blank sky, it is done in a quads pattern : obj, sky sky obj with data reduced by subracting in pairs.  It can therefore be reduced using the standard quads recipe for reduction.\n\nOther recipes for nodding to sky less often will also be available soon.">
               </av>
            </no>
            <oc name=new type=oc subtype=DRRecipe>
               <av name=ObjectRecipe descr="No Description">
                              <val value="SOURCE_WITH_NOD_TO_BLANK_SKY">
               </av>
               <av name=DRRecipe descr="No Description">
                              <val value="JITTER5_SELF_FLAT">
               </av>
               <av name=DarkRecipe descr="No Description">
                              <val value="REDUCE_DARK">
               </av>
               <av name=SkyInGroup descr="No Description">
                              <val value="false">
               </av>
               <av name=.unique descr="No Description">
                              <val value="true">
               </av>
               <av name=ObjectInGroup descr="No Description">
                              <val value="true">
               </av>
               <av name=FlatRecipe descr="No Description">
                              <val value="REDUCE_FLAT">
               </av>
               <av name=DarkInGroup descr="No Description">
                              <val value="false">
               </av>
               <av name=SkyRecipe descr="No Description">
                              <val value="REDUCE_SKY">
               </av>
               <av name=ArcRecipe descr="No Description">
                              <val value="REDUCE_ARC">
               </av>
               <av name=BiasInGroup descr="No Description">
                              <val value="false">
               </av>
               <av name=title descr="No Description">
                              <val value="SOURCE_WITH_NOD_TO_BLANK_SKY">
               </av>
               <av name=FlatInGroup descr="No Description">
                              <val value="false">
               </av>
               <av name=BiasRecipe descr="No Description">
                              <val value="REDUCE_BIAS">
               </av>
               <av name=ArcInGroup descr="No Description">
                              <val value="false">
               </av>
            </oc>
            <if name=new type=if subtype=none>
               <av name=.gui.collapsed descr="No Description">
                              <val value="false">
               </av>
               <no name=new type=no subtype=none>
                  <av name=title descr="No Description">
                                    <val value="set the number of repeats">
                  </av>
                  <av name=note descr="No Description">
                                    <val value="To repeat the quads more than once on your target , use the repeat item below to specify how many times you want to repeat it.\n\n">
                  </av>
               </no>
               <ic name=new type=ic subtype=repeat>
                  <av name=repeatCount descr="No Description">
                                    <val value="20">
                  </av>
                  <av name=title descr="No Description">
                                    <val value="Repeat 20X">
                  </av>
                  <av name=.gui.collapsed descr="No Description">
                                    <val value="false">
                  </av>
                  <no name=new type=no subtype=none>
                     <av name=title descr="No Description">
                                          <val value="offset info">
                     </av>
                     <av name=note descr="No Description">
                                          <val value="this offsets 60 arc sec perpendicular to the slit to take you to blank sky.\n\nThe sky obsrvations are specified as sky by the \"sky observe eye\" instead of the \"observe eye\" this ensures that blank sky frames are properly labelled in their headers.\n\nIf you want to offset further, edit the offset value for q in the 2nd and third offset items below.  \n\nNote that offsets are entered in the co-ordinate frame of THE SLIT.\n\n">
                     </av>
                  </no>
                  <ic name=new type=ic subtype=offset>
                     <av name=offsetPositions descr="No Description">
                                          <val value="Offset0">
                     </av>
                     <av name=Offset0 descr="No Description">
                                          <val value="0.0">
                                          <val value="0.0">
                     </av>
                     <av name=.gui.selectedOffsetPos descr="No Description">
                                          <val value="Offset0">
                     </av>
                     <ic name=new type=ic subtype=observe>
                        <av name=repeatCount descr="No Description">
                                                <val value="1">
                        </av>
                     </ic>
                  </ic>
                  <ic name=new type=ic subtype=offset>
                     <av name=.gui.selectedOffsetPos descr="No Description">
                                          <val value="Offset0">
                     </av>
                     <av name=Offset0 descr="No Description">
                                          <val value="60.0">
                                          <val value="0.0">
                     </av>
                     <av name=offsetPositions descr="No Description">
                                          <val value="Offset0">
                     </av>
                     <av name=.gui.collapsed descr="No Description">
                                          <val value="false">
                     </av>
                     <ic name=new type=ic subtype=sky>
                        <av name=repeatCount descr="No Description">
                                                <val value="2">
                        </av>
                     </ic>
                  </ic>
                  <ic name=new type=ic subtype=offset>
                     <av name=.gui.selectedOffsetPos descr="No Description">
                                          <val value="Offset0">
                     </av>
                     <av name=Offset0 descr="No Description">
                                          <val value="0.0">
                                          <val value="0.0">
                     </av>
                     <av name=offsetPositions descr="No Description">
                                          <val value="Offset0">
                     </av>
                     <av name=.gui.collapsed descr="No Description">
                                          <val value="false">
                     </av>
                     <ic name=new type=ic subtype=observe>
                        <av name=repeatCount descr="No Description">
                                                <val value="1">
                        </av>
                     </ic>
                  </ic>
               </ic>
            </if>
         </ob>
      </lf>
      <lf name=new type=lf subtype=none>
         <av name=title descr="No Description">
                  <val value="TUFTI/IRCAM3 library">
         </av>
         <av name=.gui.collapsed descr="No Description">
                  <val value="false">
         </av>
         <no name=new type=no subtype=none>
            <av name=title descr="No Description">
                        <val value="selections in this folder">
            </av>
            <av name=note descr="No Description">
                        <val value="The observations below contain aselection of standrad jitter patterns for use with IRCAM3/TUFTI\n\n\nCopy the one you want to use to your science program and edit in the values you need.">
            </av>
         </no>
         <ob name=new type=ob subtype=none>
            <av name=title descr="No Description">
                        <val value="array tests">
            </av>
            <av name=standard descr="No Description">
                        <val value="false">
            </av>
            <av name=chainedToPrev descr="No Description">
                        <val value="false">
            </av>
            <av name=chainedToNext descr="No Description">
                        <val value="false">
            </av>
            <av name=.gui.collapsed descr="No Description">
                        <val value="true">
            </av>
            <oc name=new type=oc subtype=inst.IRCAM3>
               <av name=instPort descr="No Description">
                              <val value="West">
               </av>
               <av name=sourceMag descr="No Description">
                              <val value="none">
               </av>
               <av name=.unique descr="No Description">
                              <val value="true">
               </av>
               <av name=filter descr="No Description">
                              <val value="K98">
               </av>
               <av name=posAngle descr="No Description">
                              <val value="0.0">
               </av>
               <av name=coadds descr="No Description">
                              <val value="1">
               </av>
               <av name=instAper descr="No Description">
                              <val value="-0.31">
                              <val value="11.77">
                              <val value="0.0">
                              <val value="2.150">
               </av>
               <av name=exposureTime descr="No Description">
                              <val value="1">
               </av>
               <av name=readoutArea descr="No Description">
                              <val value="256x256">
               </av>
               <av name=acqMode descr="No Description">
                              <val value="Standard+STARE">
               </av>
               <av name=.version descr="No Description">
                              <val value="1.0">
               </av>
            </oc>
            <oc name=new type=oc subtype=DRRecipe>
               <av name=ObjectRecipe descr="No Description">
                              <val value="ARRAY_TESTS">
               </av>
               <av name=DarkRecipe descr="No Description">
                              <val value="ARRAY_TESTS">
               </av>
               <av name=SkyInGroup descr="No Description">
                              <val value="false">
               </av>
               <av name=.unique descr="No Description">
                              <val value="true">
               </av>
               <av name=ObjectInGroup descr="No Description">
                              <val value="true">
               </av>
               <av name=FlatRecipe descr="No Description">
                              <val value="REDUCE_FLAT">
               </av>
               <av name=DarkInGroup descr="No Description">
                              <val value="true">
               </av>
               <av name=SkyRecipe descr="No Description">
                              <val value="REDUCE_SKY">
               </av>
               <av name=ArcRecipe descr="No Description">
                              <val value="REDUCE_ARC">
               </av>
               <av name=BiasInGroup descr="No Description">
                              <val value="true">
               </av>
               <av name=title descr="No Description">
                              <val value="ARRAY_TESTS">
               </av>
               <av name=FlatInGroup descr="No Description">
                              <val value="false">
               </av>
               <av name=BiasRecipe descr="No Description">
                              <val value="ARRAY_TESTS">
               </av>
               <av name=ArcInGroup descr="No Description">
                              <val value="false">
               </av>
            </oc>
            <if name=new type=if subtype=none>
               <av name=.gui.collapsed descr="No Description">
                              <val value="false">
               </av>
               <no name=new type=no subtype=none>
                  <av name=title descr="No Description">
                                    <val value="darks">
                  </av>
                  <av name=note descr="No Description">
                                    <val value="3 x 1sec stare darks, so first can be thrown away\n\n\nthen 2 x 1 sec ndstare darks\n\nthen a 60sec and a 10sec dark to calculate the dark current.">
                  </av>
               </no>
               <ic name=new type=ic subtype=darkObs>
                  <av name=coadds descr="No Description">
                                    <val value="1">
                  </av>
                  <av name=repeatCount descr="No Description">
                                    <val value="3">
                  </av>
                  <av name=exposureTime descr="No Description">
                                    <val value="1.0">
                  </av>
               </ic>
               <ic name=new type=ic subtype=instIRCAM3>
                  <av name=iterConfigList descr="No Description">
                                    <val value="acqModeIter">
                  </av>
                  <av name=acqModeIter descr="No Description">
                                    <val value="Standard+NDSTARE">
                  </av>
               </ic>
               <ic name=new type=ic subtype=darkObs>
                  <av name=coadds descr="No Description">
                                    <val value="1">
                  </av>
                  <av name=repeatCount descr="No Description">
                                    <val value="2">
                  </av>
                  <av name=exposureTime descr="No Description">
                                    <val value="1.0">
                  </av>
               </ic>
               <ic name=new type=ic subtype=instIRCAM3>
                  <av name=iterConfigList descr="No Description">
                                    <val value="acqModeIter">
                  </av>
                  <av name=acqModeIter descr="No Description">
                                    <val value="Standard+STARE">
                  </av>
               </ic>
               <ic name=new type=ic subtype=darkObs>
                  <av name=coadds descr="No Description">
                                    <val value="1">
                  </av>
                  <av name=repeatCount descr="No Description">
                                    <val value="1">
                  </av>
                  <av name=exposureTime descr="No Description">
                                    <val value="60.0">
                  </av>
               </ic>
               <ic name=new type=ic subtype=darkObs>
                  <av name=coadds descr="No Description">
                                    <val value="1">
                  </av>
                  <av name=repeatCount descr="No Description">
                                    <val value="1">
                  </av>
                  <av name=exposureTime descr="No Description">
                                    <val value="10.0">
                  </av>
               </ic>
               <ic name=new type=ic subtype=instIRCAM3>
                  <av name=exposureTimeIter descr="No Description">
                                    <val value="1">
                  </av>
                  <av name=iterConfigList descr="No Description">
                                    <val value="acqModeIter">
                                    <val value="exposureTimeIter">
                  </av>
                  <av name=acqModeIter descr="No Description">
                                    <val value="Standard+STARE">
                  </av>
               </ic>
               <ic name=new type=ic subtype=darkObs>
                  <av name=coadds descr="No Description">
                                    <val value="1">
                  </av>
                  <av name=repeatCount descr="No Description">
                                    <val value="1">
                  </av>
                  <av name=exposureTime descr="No Description">
                                    <val value="1.0">
                  </av>
               </ic>
            </if>
         </ob>
         <ob name=new type=ob subtype=none>
            <av name=title descr="No Description">
                        <val value="sky and Jitter">
            </av>
            <av name=standard descr="No Description">
                        <val value="false">
            </av>
            <av name=chainedToPrev descr="No Description">
                        <val value="false">
            </av>
            <av name=chainedToNext descr="No Description">
                        <val value="false">
            </av>
            <av name=.gui.collapsed descr="No Description">
                        <val value="true">
            </av>
            <no name=new type=no subtype=none>
               <av name=title descr="No Description">
                              <val value="target information">
               </av>
               <av name=note descr="No Description">
                              <val value="enter your target co-ordinates into the target list below.  Note that although this is called a \"target list\" it really only allows you to specify ONE astronomical target\n\nWhen you have done so you can use the \"position editor\" to  select a guide star, to fine tune the co-ordinate based on a sky survey picture, examine offset locations on the field etc.\n\nFor a step by step guide to how to use the position editor use \"help\".">
               </av>
            </no>
            <oc name=new type=oc subtype=targetList>
               <av name=Base descr="No Description">
                              <val value="Base">
                              <val value="enter your object name">
                              <val value="0:00:00">
                              <val value="0:00:00">
                              <val value="FK5 (J2000)">
               </av>
               <av name=.unique descr="No Description">
                              <val value="true">
               </av>
               <av name=.gui.selectedTelescopePos descr="No Description">
                              <val value="Base">
               </av>
            </oc>
            <no name=new type=no subtype=none>
               <av name=title descr="No Description">
                              <val value="how to set the exposure time and filter">
               </av>
               <av name=note descr="No Description">
                              <val value="The IRCAM3 component below uses the nbM filter.  To change it move the highlight to the \"ufti component\" below and select the filter you want to use.\n\nThe IRCAM3 component below uses a 0.5second expsoure time.   To change it move the highlight to \"ufti\" below and change the exposure time - either enter the value you want to use, or enter the magnitude range of your target and the recommended exposure time will be automatically entered.\n\n">
               </av>
            </no>
            <oc name=new type=oc subtype=inst.IRCAM3>
               <av name=instPort descr="No Description">
                              <val value="West">
               </av>
               <av name=sourceMag descr="No Description">
                              <val value="8-9">
               </av>
               <av name=.unique descr="No Description">
                              <val value="true">
               </av>
               <av name=filter descr="No Description">
                              <val value="M98">
               </av>
               <av name=posAngle descr="No Description">
                              <val value="0.0">
               </av>
               <av name=coadds descr="No Description">
                              <val value="42">
               </av>
               <av name=instAper descr="No Description">
                              <val value="6.35">
                              <val value="11.77">
                              <val value="0.0">
                              <val value="4.800">
               </av>
               <av name=exposureTime descr="No Description">
                              <val value="0.12">
               </av>
               <av name=readoutArea descr="No Description">
                              <val value="256x256">
               </av>
               <av name=acqMode descr="No Description">
                              <val value="Standard+STARE">
               </av>
               <av name=.version descr="No Description">
                              <val value="1.0">
               </av>
            </oc>
            <no name=new type=no subtype=none>
               <av name=title descr="No Description">
                              <val value="DR Recipe Selection">
               </av>
               <av name=note descr="No Description">
                              <val value="The data reduction recipe used assumes that a flat field has already been created, It will not work unless you have done this.  Use the observation \"make sky flats\" to do this.">
               </av>
            </no>
            <oc name=new type=oc subtype=DRRecipe>
               <av name=ObjectRecipe descr="No Description">
                              <val value="SKY_AND_JITTER5_APHOT">
               </av>
               <av name=DRRecipe descr="No Description">
                              <val value="SKY_AND_JITTER5_APHOT">
               </av>
               <av name=DarkRecipe descr="No Description">
                              <val value="REDUCE_DARK">
               </av>
               <av name=SkyInGroup descr="No Description">
                              <val value="true">
               </av>
               <av name=.unique descr="No Description">
                              <val value="true">
               </av>
               <av name=ObjectInGroup descr="No Description">
                              <val value="true">
               </av>
               <av name=FlatRecipe descr="No Description">
                              <val value="REDUCE_FLAT">
               </av>
               <av name=DarkInGroup descr="No Description">
                              <val value="false">
               </av>
               <av name=SkyRecipe descr="No Description">
                              <val value="REDUCE_SKY">
               </av>
               <av name=ArcRecipe descr="No Description">
                              <val value="REDUCE_ARC">
               </av>
               <av name=BiasInGroup descr="No Description">
                              <val value="false">
               </av>
               <av name=title descr="No Description">
                              <val value="SKY_AND_JITTER5_APHOT">
               </av>
               <av name=FlatInGroup descr="No Description">
                              <val value="false">
               </av>
               <av name=BiasRecipe descr="No Description">
                              <val value="REDUCE_BIAS">
               </av>
               <av name=ArcInGroup descr="No Description">
                              <val value="false">
               </av>
            </oc>
            <if name=new type=if subtype=none>
               <av name=.gui.collapsed descr="No Description">
                              <val value="false">
               </av>
               <no name=new type=no subtype=none>
                  <av name=title descr="No Description">
                                    <val value="change dark exp time here">
                  </av>
                  <av name=note descr="No Description">
                                    <val value="IF you changed the IRCAMexposurte time in the componet above then change the  dark exp time below to match it\n\nNB :\nThe data reduction recipe will not work unless you have a dark with the right exposure time">
                  </av>
               </no>
               <ic name=new type=ic subtype=darkObs>
                  <av name=coadds descr="No Description">
                                    <val value="1">
                  </av>
                  <av name=repeatCount descr="No Description">
                                    <val value="1">
                  </av>
                  <av name=exposureTime descr="No Description">
                                    <val value="0.03">
                  </av>
               </ic>
               <no name=new type=no subtype=none>
                  <av name=title descr="No Description">
                                    <val value="how to do more than one">
                  </av>
                  <av name=note descr="No Description">
                                    <val value="To repeat the pattern more than once on your target , use the repeat item below to specify how many times you want to repeat it.\n\n">
                  </av>
               </no>
               <ic name=new type=ic subtype=repeat>
                  <av name=repeatCount descr="No Description">
                                    <val value="1">
                  </av>
                  <av name=title descr="No Description">
                                    <val value="Repeat 1X">
                  </av>
                  <av name=.gui.collapsed descr="No Description">
                                    <val value="false">
                  </av>
                  <no name=new type=no subtype=none>
                     <av name=title descr="No Description">
                                          <val value="offset info">
                     </av>
                     <av name=note descr="No Description">
                                          <val value="The first offset is to \"sky\"/\"off the array\"\n\nIf you want to use a different size of offset, move the highlight to the offset\nitem below and edit them.\n\nChanging the total number of offsets\nwill break the data reduction recipe \n\nRemoving the offset to sky will break the recipe, as will changing the order !\n\n">
                     </av>
                  </no>
                  <ic name=new type=ic subtype=offset>
                     <av name=.gui.selectedOffsetPos descr="No Description">
                                          <val value="Offset0">
                     </av>
                     <av name=Offset0 descr="No Description">
                                          <val value="60.0">
                                          <val value="60.0">
                     </av>
                     <av name=offsetPositions descr="No Description">
                                          <val value="Offset0">
                     </av>
                     <av name=title descr="No Description">
                                          <val value="sky first">
                     </av>
                     <ic name=new type=ic subtype=sky>
                        <av name=repeatCount descr="No Description">
                                                <val value="1">
                        </av>
                     </ic>
                  </ic>
                  <ic name=new type=ic subtype=offset>
                     <av name=Offset8 descr="No Description">
                                          <val value="0.0">
                                          <val value="0.0">
                     </av>
                     <av name=Offset7 descr="No Description">
                                          <val value="11.0">
                                          <val value="-11.0">
                     </av>
                     <av name=Offset5 descr="No Description">
                                          <val value="-11.0">
                                          <val value="-10.0">
                     </av>
                     <av name=Offset3 descr="No Description">
                                          <val value="-10.0">
                                          <val value="11.0">
                     </av>
                     <av name=title descr="No Description">
                                          <val value="now do jitter_5">
                     </av>
                     <av name=.gui.selectedOffsetPos descr="No Description">
                                          <val value="Offset8">
                     </av>
                     <av name=Offset1 descr="No Description">
                                          <val value="10.0">
                                          <val value="10.0">
                     </av>
                     <av name=offsetPositions descr="No Description">
                                          <val value="Offset8">
                                          <val value="Offset1">
                                          <val value="Offset3">
                                          <val value="Offset5">
                                          <val value="Offset7">
                     </av>
                     <av name=.gui.collapsed descr="No Description">
                                          <val value="false">
                     </av>
                     <ic name=new type=ic subtype=observe>
                        <av name=repeatCount descr="No Description">
                                                <val value="1">
                        </av>
                     </ic>
                  </ic>
               </ic>
               <no name=new type=no subtype=none>
                  <av name=title descr="No Description">
                                    <val value="last offset">
                  </av>
                  <av name=note descr="No Description">
                                    <val value="This last offset is not in the \"repeat loop\" because it just takes the telescope back to 00 after all the observations have been taken, for convenience.">
                  </av>
               </no>
               <ic name=new type=ic subtype=offset>
                  <av name=offsetPositions descr="No Description">
                                    <val value="Offset0">
                  </av>
                  <av name=Offset0 descr="No Description">
                                    <val value="0.0">
                                    <val value="0.0">
                  </av>
                  <av name=.gui.selectedOffsetPos descr="No Description">
                                    <val value="Offset0">
                  </av>
               </ic>
            </if>
         </ob>
         <ob name=new type=ob subtype=none>
            <av name=title descr="No Description">
                        <val value="5pt jitter/offsets of 5arcsec">
            </av>
            <av name=standard descr="No Description">
                        <val value="false">
            </av>
            <av name=chainedToPrev descr="No Description">
                        <val value="false">
            </av>
            <av name=chainedToNext descr="No Description">
                        <val value="false">
            </av>
            <av name=.gui.collapsed descr="No Description">
                        <val value="true">
            </av>
            <no name=new type=no subtype=none>
               <av name=title descr="No Description">
                              <val value="target information">
               </av>
               <av name=note descr="No Description">
                              <val value="enter your target co-ordinates into the target list below.  Note that although this is called a \"target list\" it really only allows you to specify ONE astronomical target\n\nWhen you have done so you can use the \"position editor\" to  select a guide star, to fine tune the co-ordinate based on a sky survey picture, examine offset locations on the field etc.\n\nFor a step by step guide to how to use the position editor use \"help\".">
               </av>
            </no>
            <oc name=new type=oc subtype=targetList>
               <av name=Base descr="No Description">
                              <val value="Base">
                              <val value="enter your object name">
                              <val value="0:00:00">
                              <val value="0:00:00">
                              <val value="FK5 (J2000)">
               </av>
               <av name=.unique descr="No Description">
                              <val value="true">
               </av>
               <av name=.gui.selectedTelescopePos descr="No Description">
                              <val value="Base">
               </av>
            </oc>
            <no name=new type=no subtype=none>
               <av name=title descr="No Description">
                              <val value="how to set the exposure time and filter">
               </av>
               <av name=note descr="No Description">
                              <val value="The IRCAM component below uses the L\' filter.  To change it move the highlight to the \"ufti component\" below and select the filter you want to use.\n\nThe IRCAM component below uses a 0.2 second expsoure time.   To change it move the highlight to \"ufti\" below and change the exposure time - either enter the value you want to use, or enter the magnitude range of your target and a recommended exposure time will be automatically entered.\n\n">
               </av>
            </no>
            <oc name=new type=oc subtype=inst.IRCAM3>
               <av name=instPort descr="No Description">
                              <val value="West">
               </av>
               <av name=sourceMag descr="No Description">
                              <val value="BL">
               </av>
               <av name=.unique descr="No Description">
                              <val value="true">
               </av>
               <av name=filter descr="No Description">
                              <val value="L98">
               </av>
               <av name=posAngle descr="No Description">
                              <val value="0.0">
               </av>
               <av name=coadds descr="No Description">
                              <val value="100">
               </av>
               <av name=instAper descr="No Description">
                              <val value="6.35">
                              <val value="11.77">
                              <val value="0.0">
                              <val value="1.0">
               </av>
               <av name=exposureTime descr="No Description">
                              <val value="0.2">
               </av>
               <av name=readoutArea descr="No Description">
                              <val value="256x256">
               </av>
               <av name=acqMode descr="No Description">
                              <val value="Deepwell+STARE">
               </av>
               <av name=.version descr="No Description">
                              <val value="1.0">
               </av>
            </oc>
            <no name=new type=no subtype=none>
               <av name=title descr="No Description">
                              <val value="DR Recipe Selection">
               </av>
               <av name=note descr="No Description">
                              <val value="The data reduction recipe specified below uses the most basic data reduction recipe for speed.  (this is a good idea if your exposure time short).\n\nIf you need to use a more complete recipe then select JITTER5_SELF_FLAT_APHOT\n">
               </av>
            </no>
            <oc name=new type=oc subtype=DRRecipe>
               <av name=ObjectRecipe descr="No Description">
                              <val value="JITTER5_SELF_FLAT">
               </av>
               <av name=DRRecipe descr="No Description">
                              <val value="JITTER5_SELF_FLAT">
               </av>
               <av name=DarkRecipe descr="No Description">
                              <val value="REDUCE_DARK">
               </av>
               <av name=SkyInGroup descr="No Description">
                              <val value="true">
               </av>
               <av name=.unique descr="No Description">
                              <val value="true">
               </av>
               <av name=ObjectInGroup descr="No Description">
                              <val value="true">
               </av>
               <av name=FlatRecipe descr="No Description">
                              <val value="REDUCE_FLAT">
               </av>
               <av name=DarkInGroup descr="No Description">
                              <val value="false">
               </av>
               <av name=SkyRecipe descr="No Description">
                              <val value="REDUCE_SKY">
               </av>
               <av name=ArcRecipe descr="No Description">
                              <val value="REDUCE_ARC">
               </av>
               <av name=BiasInGroup descr="No Description">
                              <val value="false">
               </av>
               <av name=title descr="No Description">
                              <val value="JITTER5_SELF_FLAT">
               </av>
               <av name=FlatInGroup descr="No Description">
                              <val value="false">
               </av>
               <av name=BiasRecipe descr="No Description">
                              <val value="REDUCE_BIAS">
               </av>
               <av name=ArcInGroup descr="No Description">
                              <val value="false">
               </av>
            </oc>
            <if name=new type=if subtype=none>
               <av name=.gui.collapsed descr="No Description">
                              <val value="false">
               </av>
               <no name=new type=no subtype=none>
                  <av name=title descr="No Description">
                                    <val value="set dark exp time here">
                  </av>
                  <av name=note descr="No Description">
                                    <val value="If you changed the dark exposure time\nin the IRCAM configuration aobe remember to change it here too\n\nNB :\nThe data reduction recipe will not work unless you have a dark with the right exposure time\n\nUse the reset to default button to ensure the dark exposure time matches that in yourIRCAM setting above,">
                  </av>
               </no>
               <ic name=new type=ic subtype=darkObs>
                  <av name=coadds descr="No Description">
                                    <val value="100">
                  </av>
                  <av name=repeatCount descr="No Description">
                                    <val value="1">
                  </av>
                  <av name=exposureTime descr="No Description">
                                    <val value="0.2">
                  </av>
               </ic>
               <no name=new type=no subtype=none>
                  <av name=title descr="No Description">
                                    <val value="how to do more than one 5-point jitters">
                  </av>
                  <av name=note descr="No Description">
                                    <val value="To repeat the 5 point jitter pattern more than once on your target , use the repeat item below to specify how many times you want to repeat it.\n\n">
                  </av>
               </no>
               <ic name=new type=ic subtype=repeat>
                  <av name=.gui.collapsed descr="No Description">
                                    <val value="false">
                  </av>
                  <no name=new type=no subtype=none>
                     <av name=title descr="No Description">
                                          <val value="offset info">
                     </av>
                     <av name=note descr="No Description">
                                          <val value="\n\nIf you want to use a different size of offset, move the highlight to the offset\nitem below and edit them.">
                     </av>
                  </no>
                  <ic name=new type=ic subtype=offset>
                     <av name=Offset4 descr="No Description">
                                          <val value="5.0">
                                          <val value="-5.0">
                     </av>
                     <av name=.gui.selectedOffsetPos descr="No Description">
                                          <val value="Offset0">
                     </av>
                     <av name=Offset3 descr="No Description">
                                          <val value="-5.0">
                                          <val value="-5.0">
                     </av>
                     <av name=Offset2 descr="No Description">
                                          <val value="-5.0">
                                          <val value="5.0">
                     </av>
                     <av name=Offset1 descr="No Description">
                                          <val value="5.0">
                                          <val value="5.0">
                     </av>
                     <av name=Offset0 descr="No Description">
                                          <val value="0.0">
                                          <val value="0.0">
                     </av>
                     <av name=offsetPositions descr="No Description">
                                          <val value="Offset0">
                                          <val value="Offset1">
                                          <val value="Offset2">
                                          <val value="Offset3">
                                          <val value="Offset4">
                     </av>
                     <av name=title descr="No Description">
                                          <val value="5point jitter/offsets of 5 arc sec">
                     </av>
                     <ic name=new type=ic subtype=observe>
                        <av name=repeatCount descr="No Description">
                                                <val value="1">
                        </av>
                     </ic>
                  </ic>
               </ic>
               <no name=new type=no subtype=none>
                  <av name=title descr="No Description">
                                    <val value="last offset">
                  </av>
                  <av name=note descr="No Description">
                                    <val value="This last offset simply takes the telescope back to 00 after all the repeats of the jitter pattern are finished">
                  </av>
               </no>
               <ic name=new type=ic subtype=offset>
                  <av name=offsetPositions descr="No Description">
                                    <val value="Offset0">
                  </av>
                  <av name=Offset0 descr="No Description">
                                    <val value="0.0">
                                    <val value="0.0">
                  </av>
                  <av name=.gui.selectedOffsetPos descr="No Description">
                                    <val value="Offset0">
                  </av>
               </ic>
            </if>
         </ob>
         <ob name=new type=ob subtype=none>
            <av name=title descr="No Description">
                        <val value="5pt jitter/offsets of 6arcsec">
            </av>
            <av name=standard descr="No Description">
                        <val value="false">
            </av>
            <av name=chainedToPrev descr="No Description">
                        <val value="false">
            </av>
            <av name=chainedToNext descr="No Description">
                        <val value="false">
            </av>
            <av name=.gui.collapsed descr="No Description">
                        <val value="true">
            </av>
            <no name=new type=no subtype=none>
               <av name=title descr="No Description">
                              <val value="target information">
               </av>
               <av name=note descr="No Description">
                              <val value="enter your target co-ordinates into the target list below.  Note that although this is called a \"target list\" it really only allows you to specify ONE astronomical target\n\nWhen you have done so you can use the \"position editor\" to  select a guide star, to fine tune the co-ordinate based on a sky survey picture, examine offset locations on the field etc.\n\nFor a step by step guide to how to use the position editor use \"help\".">
               </av>
            </no>
            <oc name=new type=oc subtype=targetList>
               <av name=Base descr="No Description">
                              <val value="Base">
                              <val value="enter your object name">
                              <val value="0:00:00">
                              <val value="0:00:00">
                              <val value="FK5 (J2000)">
               </av>
               <av name=.unique descr="No Description">
                              <val value="true">
               </av>
               <av name=.gui.selectedTelescopePos descr="No Description">
                              <val value="Base">
               </av>
            </oc>
            <no name=new type=no subtype=none>
               <av name=title descr="No Description">
                              <val value="how to set the exposure time and filter">
               </av>
               <av name=note descr="No Description">
                              <val value="The IRCAM component below uses the L\' filter.  To change it move the highlight to the \"ufti component\" below and select the filter you want to use.\n\nThe IRCAM component below uses a 0.2 second expsoure time.   To change it move the highlight to \"ufti\" below and change the exposure time - either enter the value you want to use, or enter the magnitude range of your target and a recommended exposure time will be automatically entered.\n\n">
               </av>
            </no>
            <oc name=new type=oc subtype=inst.IRCAM3>
               <av name=instPort descr="No Description">
                              <val value="West">
               </av>
               <av name=sourceMag descr="No Description">
                              <val value="BL">
               </av>
               <av name=.unique descr="No Description">
                              <val value="true">
               </av>
               <av name=filter descr="No Description">
                              <val value="L98">
               </av>
               <av name=posAngle descr="No Description">
                              <val value="0.0">
               </av>
               <av name=coadds descr="No Description">
                              <val value="100">
               </av>
               <av name=instAper descr="No Description">
                              <val value="6.35">
                              <val value="11.77">
                              <val value="0.0">
                              <val value="1.0">
               </av>
               <av name=exposureTime descr="No Description">
                              <val value="0.2">
               </av>
               <av name=readoutArea descr="No Description">
                              <val value="256x256">
               </av>
               <av name=acqMode descr="No Description">
                              <val value="Deepwell+STARE">
               </av>
               <av name=.version descr="No Description">
                              <val value="1.0">
               </av>
            </oc>
            <no name=new type=no subtype=none>
               <av name=title descr="No Description">
                              <val value="DR Recipe Selection">
               </av>
               <av name=note descr="No Description">
                              <val value="The data reduction recipe specified below uses the most basic data reduction recipe for speed.  (this is a good idea if your exposure time short).\n\nIf you need to use a more complete recipe then select JITTER5_SELF_FLAT_APHOT\n">
               </av>
            </no>
            <oc name=new type=oc subtype=DRRecipe>
               <av name=ObjectRecipe descr="No Description">
                              <val value="JITTER5_SELF_FLAT">
               </av>
               <av name=DRRecipe descr="No Description">
                              <val value="JITTER5_SELF_FLAT">
               </av>
               <av name=DarkRecipe descr="No Description">
                              <val value="REDUCE_DARK">
               </av>
               <av name=SkyInGroup descr="No Description">
                              <val value="true">
               </av>
               <av name=.unique descr="No Description">
                              <val value="true">
               </av>
               <av name=ObjectInGroup descr="No Description">
                              <val value="true">
               </av>
               <av name=FlatRecipe descr="No Description">
                              <val value="REDUCE_FLAT">
               </av>
               <av name=DarkInGroup descr="No Description">
                              <val value="false">
               </av>
               <av name=SkyRecipe descr="No Description">
                              <val value="REDUCE_SKY">
               </av>
               <av name=ArcRecipe descr="No Description">
                              <val value="REDUCE_ARC">
               </av>
               <av name=BiasInGroup descr="No Description">
                              <val value="false">
               </av>
               <av name=title descr="No Description">
                              <val value="JITTER5_SELF_FLAT">
               </av>
               <av name=FlatInGroup descr="No Description">
                              <val value="false">
               </av>
               <av name=BiasRecipe descr="No Description">
                              <val value="REDUCE_BIAS">
               </av>
               <av name=ArcInGroup descr="No Description">
                              <val value="false">
               </av>
            </oc>
            <if name=new type=if subtype=none>
               <av name=.gui.collapsed descr="No Description">
                              <val value="false">
               </av>
               <no name=new type=no subtype=none>
                  <av name=title descr="No Description">
                                    <val value="set dark exp time here">
                  </av>
                  <av name=note descr="No Description">
                                    <val value="If you changed the dark exposure time\nin the IRCAM configuration aobe remember to change it here too\n\nNB :\nThe data reduction recipe will not work unless you have a dark with the right exposure time\n\nUse the reset to default button to ensure the dark exposure time matches that in yourIRCAM setting above,">
                  </av>
               </no>
               <ic name=new type=ic subtype=darkObs>
                  <av name=coadds descr="No Description">
                                    <val value="100">
                  </av>
                  <av name=repeatCount descr="No Description">
                                    <val value="1">
                  </av>
                  <av name=exposureTime descr="No Description">
                                    <val value="0.2">
                  </av>
               </ic>
               <no name=new type=no subtype=none>
                  <av name=title descr="No Description">
                                    <val value="how to do more than one 5-point jitters">
                  </av>
                  <av name=note descr="No Description">
                                    <val value="To repeat the 5 point jitter pattern more than once on your target , use the repeat item below to specify how many times you want to repeat it.\n\n">
                  </av>
               </no>
               <ic name=new type=ic subtype=repeat>
                  <av name=.gui.collapsed descr="No Description">
                                    <val value="false">
                  </av>
                  <no name=new type=no subtype=none>
                     <av name=title descr="No Description">
                                          <val value="offset info">
                     </av>
                     <av name=note descr="No Description">
                                          <val value="\n\nIf you want to use a different size of offset, move the highlight to the offset\nitem below and edit them.">
                     </av>
                  </no>
                  <ic name=new type=ic subtype=offset>
                     <av name=Offset8 descr="No Description">
                                          <val value="0.0">
                                          <val value="0.0">
                     </av>
                     <av name=Offset7 descr="No Description">
                                          <val value="-6.0">
                                          <val value="-6.0">
                     </av>
                     <av name=Offset5 descr="No Description">
                                          <val value="-6.0">
                                          <val value="6.0">
                     </av>
                     <av name=Offset3 descr="No Description">
                                          <val value="6.0">
                                          <val value="6.0">
                     </av>
                     <av name=title descr="No Description">
                                          <val value="jitter_5_6as">
                     </av>
                     <av name=.gui.selectedOffsetPos descr="No Description">
                                          <val value="Offset8">
                     </av>
                     <av name=Offset1 descr="No Description">
                                          <val value="6.0">
                                          <val value="-6.0">
                     </av>
                     <av name=offsetPositions descr="No Description">
                                          <val value="Offset8">
                                          <val value="Offset3">
                                          <val value="Offset5">
                                          <val value="Offset7">
                                          <val value="Offset1">
                     </av>
                     <av name=.gui.collapsed descr="No Description">
                                          <val value="false">
                     </av>
                     <ic name=new type=ic subtype=observe>
                        <av name=repeatCount descr="No Description">
                                                <val value="1">
                        </av>
                     </ic>
                  </ic>
               </ic>
               <no name=new type=no subtype=none>
                  <av name=title descr="No Description">
                                    <val value="last offset">
                  </av>
                  <av name=note descr="No Description">
                                    <val value="This last offset simply takes the telescope back to 00 after all the repeats of the jitter pattern are finished">
                  </av>
               </no>
               <ic name=new type=ic subtype=offset>
                  <av name=offsetPositions descr="No Description">
                                    <val value="Offset0">
                  </av>
                  <av name=Offset0 descr="No Description">
                                    <val value="0.0">
                                    <val value="0.0">
                  </av>
                  <av name=.gui.selectedOffsetPos descr="No Description">
                                    <val value="Offset0">
                  </av>
               </ic>
            </if>
         </ob>
         <ob name=new type=ob subtype=none>
            <av name=title descr="No Description">
                        <val value="9pt jitter/offsets of 6arcsec">
            </av>
            <av name=standard descr="No Description">
                        <val value="false">
            </av>
            <av name=chainedToPrev descr="No Description">
                        <val value="false">
            </av>
            <av name=chainedToNext descr="No Description">
                        <val value="false">
            </av>
            <av name=.gui.collapsed descr="No Description">
                        <val value="true">
            </av>
            <no name=new type=no subtype=none>
               <av name=title descr="No Description">
                              <val value="target information">
               </av>
               <av name=note descr="No Description">
                              <val value="enter your target co-ordinates into the target list below.  Note that although this is called a \"target list\" it really only allows you to specify ONE astronomical target\n\nWhen you have done so you can use the \"position editor\" to  select a guide star, to fine tune the co-ordinate based on a sky survey picture, examine offset locations on the field etc.\n\nFor a step by step guide to how to use the position editor use \"help\".">
               </av>
            </no>
            <oc name=new type=oc subtype=targetList>
               <av name=Base descr="No Description">
                              <val value="Base">
                              <val value="enter your object name">
                              <val value="0:00:00">
                              <val value="0:00:00">
                              <val value="FK5 (J2000)">
               </av>
               <av name=.unique descr="No Description">
                              <val value="true">
               </av>
               <av name=.gui.selectedTelescopePos descr="No Description">
                              <val value="Base">
               </av>
            </oc>
            <no name=new type=no subtype=none>
               <av name=title descr="No Description">
                              <val value="how to set the exposure time and filter">
               </av>
               <av name=note descr="No Description">
                              <val value="The IRCAM component below uses the L\' filter.  To change it move the highlight to the \"ufti component\" below and select the filter you want to use.\n\nThe IRCAM component below uses a 0.2 second expsoure time.   To change it move the highlight to \"ufti\" below and change the exposure time - either enter the value you want to use, or enter the magnitude range of your target and a recommended exposure time will be automatically entered.\n\n">
               </av>
            </no>
            <oc name=new type=oc subtype=inst.IRCAM3>
               <av name=instPort descr="No Description">
                              <val value="West">
               </av>
               <av name=sourceMag descr="No Description">
                              <val value="BL">
               </av>
               <av name=.unique descr="No Description">
                              <val value="true">
               </av>
               <av name=filter descr="No Description">
                              <val value="L98">
               </av>
               <av name=posAngle descr="No Description">
                              <val value="0.0">
               </av>
               <av name=coadds descr="No Description">
                              <val value="100">
               </av>
               <av name=instAper descr="No Description">
                              <val value="6.35">
                              <val value="11.77">
                              <val value="0.0">
                              <val value="1.0">
               </av>
               <av name=exposureTime descr="No Description">
                              <val value="0.2">
               </av>
               <av name=readoutArea descr="No Description">
                              <val value="256x256">
               </av>
               <av name=acqMode descr="No Description">
                              <val value="Deepwell+STARE">
               </av>
               <av name=.version descr="No Description">
                              <val value="1.0">
               </av>
            </oc>
            <no name=new type=no subtype=none>
               <av name=title descr="No Description">
                              <val value="DR Recipe Selection">
               </av>
               <av name=note descr="No Description">
                              <val value="The data reduction recipe specified below uses the most basic data reduction recipe for speed.  (this is a good idea if your exposure time short).\n\nIf you need to use a more complete recipe then select JITTER9_SELF_FLAT_APHOT\n">
               </av>
            </no>
            <oc name=new type=oc subtype=DRRecipe>
               <av name=ObjectRecipe descr="No Description">
                              <val value="JITTER9_SELF_FLAT">
               </av>
               <av name=DRRecipe descr="No Description">
                              <val value="JITTER5_SELF_FLAT">
               </av>
               <av name=DarkRecipe descr="No Description">
                              <val value="REDUCE_DARK">
               </av>
               <av name=SkyInGroup descr="No Description">
                              <val value="true">
               </av>
               <av name=.unique descr="No Description">
                              <val value="true">
               </av>
               <av name=ObjectInGroup descr="No Description">
                              <val value="true">
               </av>
               <av name=FlatRecipe descr="No Description">
                              <val value="REDUCE_FLAT">
               </av>
               <av name=DarkInGroup descr="No Description">
                              <val value="false">
               </av>
               <av name=SkyRecipe descr="No Description">
                              <val value="REDUCE_SKY">
               </av>
               <av name=ArcRecipe descr="No Description">
                              <val value="REDUCE_ARC">
               </av>
               <av name=BiasInGroup descr="No Description">
                              <val value="false">
               </av>
               <av name=title descr="No Description">
                              <val value="JITTER9_SELF_FLAT">
               </av>
               <av name=FlatInGroup descr="No Description">
                              <val value="false">
               </av>
               <av name=BiasRecipe descr="No Description">
                              <val value="REDUCE_BIAS">
               </av>
               <av name=ArcInGroup descr="No Description">
                              <val value="false">
               </av>
            </oc>
            <if name=new type=if subtype=none>
               <av name=.gui.collapsed descr="No Description">
                              <val value="false">
               </av>
               <no name=new type=no subtype=none>
                  <av name=title descr="No Description">
                                    <val value="set dark exp time here">
                  </av>
                  <av name=note descr="No Description">
                                    <val value="If you changed the dark exposure time\nin the IRCAM configuration aobe remember to change it here too\n\nNB :\nThe data reduction recipe will not work unless you have a dark with the right exposure time\n\nUse the reset to default button to ensure the dark exposure time matches that in your UFTI setting above,">
                  </av>
               </no>
               <ic name=new type=ic subtype=darkObs>
                  <av name=coadds descr="No Description">
                                    <val value="100">
                  </av>
                  <av name=repeatCount descr="No Description">
                                    <val value="1">
                  </av>
                  <av name=exposureTime descr="No Description">
                                    <val value="0.2">
                  </av>
               </ic>
               <no name=new type=no subtype=none>
                  <av name=title descr="No Description">
                                    <val value="how to do more than one 9-point jitters">
                  </av>
                  <av name=note descr="No Description">
                                    <val value="To repeat the 9 point jitter pattern more than once on your target , use the repeat item below to specify how many times you want to repeat it.\n\n">
                  </av>
               </no>
               <ic name=new type=ic subtype=repeat>
                  <av name=.gui.collapsed descr="No Description">
                                    <val value="false">
                  </av>
                  <no name=new type=no subtype=none>
                     <av name=title descr="No Description">
                                          <val value="offset info">
                     </av>
                     <av name=note descr="No Description">
                                          <val value="\n\nIf you want to use a different size of offset, move the highlight to the offset\nitem below and edit them.">
                     </av>
                  </no>
                  <ic name=new type=ic subtype=offset>
                     <av name=Offset8 descr="No Description">
                                          <val value="6.0">
                                          <val value="-6.0">
                     </av>
                     <av name=Offset7 descr="No Description">
                                          <val value="0.0">
                                          <val value="-6.0">
                     </av>
                     <av name=Offset6 descr="No Description">
                                          <val value="-6.0">
                                          <val value="-6.0">
                     </av>
                     <av name=Offset5 descr="No Description">
                                          <val value="-6.0">
                                          <val value="0.0">
                     </av>
                     <av name=Offset4 descr="No Description">
                                          <val value="-6.0">
                                          <val value="6.0">
                     </av>
                     <av name=Offset3 descr="No Description">
                                          <val value="0.0">
                                          <val value="6.0">
                     </av>
                     <av name=Offset2 descr="No Description">
                                          <val value="6.0">
                                          <val value="6.0">
                     </av>
                     <av name=.gui.selectedOffsetPos descr="No Description">
                                          <val value="Offset0">
                     </av>
                     <av name=Offset1 descr="No Description">
                                          <val value="6.0">
                                          <val value="0.0">
                     </av>
                     <av name=Offset0 descr="No Description">
                                          <val value="0.0">
                                          <val value="0.0">
                     </av>
                     <av name=offsetPositions descr="No Description">
                                          <val value="Offset0">
                                          <val value="Offset1">
                                          <val value="Offset2">
                                          <val value="Offset3">
                                          <val value="Offset4">
                                          <val value="Offset5">
                                          <val value="Offset6">
                                          <val value="Offset7">
                                          <val value="Offset8">
                     </av>
                     <ic name=new type=ic subtype=observe>
                        <av name=repeatCount descr="No Description">
                                                <val value="1">
                        </av>
                     </ic>
                  </ic>
               </ic>
               <no name=new type=no subtype=none>
                  <av name=title descr="No Description">
                                    <val value="last offset">
                  </av>
                  <av name=note descr="No Description">
                                    <val value="This last offset simply takes the telescope back to 00 after all the repeats of the jitter pattern are finished">
                  </av>
               </no>
               <ic name=new type=ic subtype=offset>
                  <av name=offsetPositions descr="No Description">
                                    <val value="Offset0">
                  </av>
                  <av name=Offset0 descr="No Description">
                                    <val value="0.0">
                                    <val value="0.0">
                  </av>
                  <av name=.gui.selectedOffsetPos descr="No Description">
                                    <val value="Offset0">
                  </av>
               </ic>
            </if>
         </ob>
         <ob name=new type=ob subtype=none>
            <av name=title descr="No Description">
                        <val value="Nod 4 /offsets of 10arcsec">
            </av>
            <av name=standard descr="No Description">
                        <val value="false">
            </av>
            <av name=chainedToPrev descr="No Description">
                        <val value="false">
            </av>
            <av name=chainedToNext descr="No Description">
                        <val value="false">
            </av>
            <av name=.gui.collapsed descr="No Description">
                        <val value="true">
            </av>
            <no name=new type=no subtype=none>
               <av name=title descr="No Description">
                              <val value="target information">
               </av>
               <av name=note descr="No Description">
                              <val value="enter your target co-ordinates into the target list below.  Note that although this is called a \"target list\" it really only allows you to specify ONE astronomical target\n\nWhen you have done so you can use the \"position editor\" to  select a guide star, to fine tune the co-ordinate based on a sky survey picture, examine offset locations on the field etc.\n\nFor a step by step guide to how to use the position editor use \"help\".">
               </av>
            </no>
            <oc name=new type=oc subtype=targetList>
               <av name=Base descr="No Description">
                              <val value="Base">
                              <val value="enter your object name">
                              <val value="0:00:00">
                              <val value="0:00:00">
                              <val value="FK5 (J2000)">
               </av>
               <av name=.unique descr="No Description">
                              <val value="true">
               </av>
               <av name=.gui.selectedTelescopePos descr="No Description">
                              <val value="Base">
               </av>
            </oc>
            <no name=new type=no subtype=none>
               <av name=title descr="No Description">
                              <val value="how to set the exposure time and filter">
               </av>
               <av name=note descr="No Description">
                              <val value="The IRCAM component below uses the L\' filter.  To change it move the highlight to the \"IRCAM component\" below and select the filter you want to use.\n\nThe IRCAM component below uses a 0.2 second expsoure time.   To change it move the highlight to IRCAM below and change the exposure time - either enter the value you want to use, or enter the magnitude range of your target and a recommended exposure time will be automatically entered.\n\n">
               </av>
            </no>
            <oc name=new type=oc subtype=inst.IRCAM3>
               <av name=instPort descr="No Description">
                              <val value="West">
               </av>
               <av name=sourceMag descr="No Description">
                              <val value="BL">
               </av>
               <av name=.unique descr="No Description">
                              <val value="true">
               </av>
               <av name=filter descr="No Description">
                              <val value="L98">
               </av>
               <av name=posAngle descr="No Description">
                              <val value="0.0">
               </av>
               <av name=coadds descr="No Description">
                              <val value="100">
               </av>
               <av name=instAper descr="No Description">
                              <val value="6.35">
                              <val value="11.77">
                              <val value="0.0">
                              <val value="1.0">
               </av>
               <av name=exposureTime descr="No Description">
                              <val value="0.2">
               </av>
               <av name=readoutArea descr="No Description">
                              <val value="256x256">
               </av>
               <av name=acqMode descr="No Description">
                              <val value="Deepwell+STARE">
               </av>
               <av name=.version descr="No Description">
                              <val value="1.0">
               </av>
            </oc>
            <no name=new type=no subtype=none>
               <av name=title descr="No Description">
                              <val value="DR Recipe Selection">
               </av>
               <av name=note descr="No Description">
                              <val value="The data reduction recipe specified below uses the most basic data reduction recipe for speed.  (this is a good idea if your exposure time short).\n\nIf you need to use a more complete recipe then select an _aphot variant on this.\n\nRemember that the recipes match the pattern of\noffsets">
               </av>
            </no>
            <oc name=new type=oc subtype=DRRecipe>
               <av name=ObjectRecipe descr="No Description">
                              <val value="NOD4_SELF_FLAT_NO_MASK">
               </av>
               <av name=DRRecipe descr="No Description">
                              <val value="JITTER5_SELF_FLAT">
               </av>
               <av name=DarkRecipe descr="No Description">
                              <val value="REDUCE_DARK">
               </av>
               <av name=SkyInGroup descr="No Description">
                              <val value="true">
               </av>
               <av name=.unique descr="No Description">
                              <val value="true">
               </av>
               <av name=ObjectInGroup descr="No Description">
                              <val value="true">
               </av>
               <av name=FlatRecipe descr="No Description">
                              <val value="REDUCE_FLAT">
               </av>
               <av name=DarkInGroup descr="No Description">
                              <val value="false">
               </av>
               <av name=SkyRecipe descr="No Description">
                              <val value="REDUCE_SKY">
               </av>
               <av name=ArcRecipe descr="No Description">
                              <val value="REDUCE_ARC">
               </av>
               <av name=BiasInGroup descr="No Description">
                              <val value="false">
               </av>
               <av name=title descr="No Description">
                              <val value="NOD4_SELF_FLAT_NO_MASK">
               </av>
               <av name=FlatInGroup descr="No Description">
                              <val value="false">
               </av>
               <av name=BiasRecipe descr="No Description">
                              <val value="REDUCE_BIAS">
               </av>
               <av name=ArcInGroup descr="No Description">
                              <val value="false">
               </av>
            </oc>
            <if name=new type=if subtype=none>
               <av name=.gui.collapsed descr="No Description">
                              <val value="false">
               </av>
               <no name=new type=no subtype=none>
                  <av name=title descr="No Description">
                                    <val value="set dark exp time here">
                  </av>
                  <av name=note descr="No Description">
                                    <val value="If you changed the dark exposure time\nin the IRCAM configuration aobe remember to change it here too\n\nNB :\nThe data reduction recipe will not work unless you have a dark with the right exposure time\n\nUse the reset to default button to ensure the dark exposure time matches that in yourIRCAM setting above,">
                  </av>
               </no>
               <ic name=new type=ic subtype=darkObs>
                  <av name=coadds descr="No Description">
                                    <val value="100">
                  </av>
                  <av name=repeatCount descr="No Description">
                                    <val value="1">
                  </av>
                  <av name=exposureTime descr="No Description">
                                    <val value="0.2">
                  </av>
               </ic>
               <no name=new type=no subtype=none>
                  <av name=title descr="No Description">
                                    <val value="how to do more than one 5-point jitters">
                  </av>
                  <av name=note descr="No Description">
                                    <val value="To repeat the quadrant jitter pattern more than once on your target , use the repeat item below to specify how many times you want to repeat it.\n\n">
                  </av>
               </no>
               <ic name=new type=ic subtype=repeat>
                  <av name=.gui.collapsed descr="No Description">
                                    <val value="false">
                  </av>
                  <no name=new type=no subtype=none>
                     <av name=title descr="No Description">
                                          <val value="offset info">
                     </av>
                     <av name=note descr="No Description">
                                          <val value="\n\nIf you want to use a different size of offset, move the highlight to the offset\nitem below and edit them.\n\nRemember that changing the number of offsets will break the reduction recipe. ">
                     </av>
                  </no>
                  <ic name=new type=ic subtype=offset>
                     <av name=.gui.selectedOffsetPos descr="No Description">
                                          <val value="Offset0">
                     </av>
                     <av name=Offset3 descr="No Description">
                                          <val value="5.0">
                                          <val value="-5.0">
                     </av>
                     <av name=Offset2 descr="No Description">
                                          <val value="5.0">
                                          <val value="5.0">
                     </av>
                     <av name=Offset1 descr="No Description">
                                          <val value="-5.0">
                                          <val value="5.0">
                     </av>
                     <av name=Offset0 descr="No Description">
                                          <val value="-5.0">
                                          <val value="-5.0">
                     </av>
                     <av name=offsetPositions descr="No Description">
                                          <val value="Offset0">
                                          <val value="Offset1">
                                          <val value="Offset2">
                                          <val value="Offset3">
                     </av>
                     <av name=title descr="No Description">
                                          <val value="nod_4_10as">
                     </av>
                     <ic name=new type=ic subtype=observe>
                        <av name=repeatCount descr="No Description">
                                                <val value="1">
                        </av>
                     </ic>
                  </ic>
               </ic>
               <no name=new type=no subtype=none>
                  <av name=title descr="No Description">
                                    <val value="last offset">
                  </av>
                  <av name=note descr="No Description">
                                    <val value="This last offset simply takes the telescope back to 00 after all the repeats of the jitter pattern are finished">
                  </av>
               </no>
               <ic name=new type=ic subtype=offset>
                  <av name=offsetPositions descr="No Description">
                                    <val value="Offset0">
                  </av>
                  <av name=Offset0 descr="No Description">
                                    <val value="0.0">
                                    <val value="0.0">
                  </av>
                  <av name=.gui.selectedOffsetPos descr="No Description">
                                    <val value="Offset0">
                  </av>
               </ic>
            </if>
         </ob>
         <ob name=new type=ob subtype=none>
            <av name=title descr="No Description">
                        <val value="Nod 8 /offsets of 5arcsec">
            </av>
            <av name=standard descr="No Description">
                        <val value="false">
            </av>
            <av name=chainedToPrev descr="No Description">
                        <val value="false">
            </av>
            <av name=chainedToNext descr="No Description">
                        <val value="false">
            </av>
            <av name=.gui.collapsed descr="No Description">
                        <val value="true">
            </av>
            <no name=new type=no subtype=none>
               <av name=title descr="No Description">
                              <val value="target information">
               </av>
               <av name=note descr="No Description">
                              <val value="enter your target co-ordinates into the target list below.  Note that although this is called a \"target list\" it really only allows you to specify ONE astronomical target\n\nWhen you have done so you can use the \"position editor\" to  select a guide star, to fine tune the co-ordinate based on a sky survey picture, examine offset locations on the field etc.\n\nFor a step by step guide to how to use the position editor use \"help\".">
               </av>
            </no>
            <oc name=new type=oc subtype=targetList>
               <av name=Base descr="No Description">
                              <val value="Base">
                              <val value="enter your object name">
                              <val value="0:00:00">
                              <val value="0:00:00">
                              <val value="FK5 (J2000)">
               </av>
               <av name=.unique descr="No Description">
                              <val value="true">
               </av>
               <av name=.gui.selectedTelescopePos descr="No Description">
                              <val value="Base">
               </av>
            </oc>
            <no name=new type=no subtype=none>
               <av name=title descr="No Description">
                              <val value="how to set the exposure time and filter">
               </av>
               <av name=note descr="No Description">
                              <val value="The IRCAM component below uses the L\' filter.  To change it move the highlight to the \"IRCAM component\" below and select the filter you want to use.\n\nThe IRCAM component below uses a 0.2 second expsoure time.   To change it move the highlight to \"IRCAM\" below and change the exposure time - either enter the value you want to use, or enter the magnitude range of your target and a recommended exposure time will be automatically entered.\n\n">
               </av>
            </no>
            <oc name=new type=oc subtype=inst.IRCAM3>
               <av name=instPort descr="No Description">
                              <val value="West">
               </av>
               <av name=sourceMag descr="No Description">
                              <val value="BL">
               </av>
               <av name=.unique descr="No Description">
                              <val value="true">
               </av>
               <av name=filter descr="No Description">
                              <val value="L98">
               </av>
               <av name=posAngle descr="No Description">
                              <val value="0.0">
               </av>
               <av name=coadds descr="No Description">
                              <val value="100">
               </av>
               <av name=instAper descr="No Description">
                              <val value="6.35">
                              <val value="11.77">
                              <val value="0.0">
                              <val value="1.0">
               </av>
               <av name=exposureTime descr="No Description">
                              <val value="0.2">
               </av>
               <av name=readoutArea descr="No Description">
                              <val value="256x256">
               </av>
               <av name=acqMode descr="No Description">
                              <val value="Deepwell+STARE">
               </av>
               <av name=.version descr="No Description">
                              <val value="1.0">
               </av>
            </oc>
            <no name=new type=no subtype=none>
               <av name=title descr="No Description">
                              <val value="DR Recipe Selection">
               </av>
               <av name=note descr="No Description">
                              <val value="The data reduction recipe specified below uses the most basic data reduction recipe for speed.  (this is a good idea if your exposure time short).\n\nIf you need to use a more complete recipe then select an _aphot variant on this.\n\nRemember that the recipes match the pattern of\noffsets">
               </av>
            </no>
            <oc name=new type=oc subtype=DRRecipe>
               <av name=ObjectRecipe descr="No Description">
                              <val value="NOD8_SELF_FLAT_NO_MASK">
               </av>
               <av name=DRRecipe descr="No Description">
                              <val value="JITTER5_SELF_FLAT">
               </av>
               <av name=DarkRecipe descr="No Description">
                              <val value="REDUCE_DARK">
               </av>
               <av name=SkyInGroup descr="No Description">
                              <val value="true">
               </av>
               <av name=.unique descr="No Description">
                              <val value="true">
               </av>
               <av name=ObjectInGroup descr="No Description">
                              <val value="true">
               </av>
               <av name=FlatRecipe descr="No Description">
                              <val value="REDUCE_FLAT">
               </av>
               <av name=DarkInGroup descr="No Description">
                              <val value="false">
               </av>
               <av name=SkyRecipe descr="No Description">
                              <val value="REDUCE_SKY">
               </av>
               <av name=ArcRecipe descr="No Description">
                              <val value="REDUCE_ARC">
               </av>
               <av name=BiasInGroup descr="No Description">
                              <val value="false">
               </av>
               <av name=title descr="No Description">
                              <val value="NOD8_SELF_FLAT_NO_MASK">
               </av>
               <av name=FlatInGroup descr="No Description">
                              <val value="false">
               </av>
               <av name=BiasRecipe descr="No Description">
                              <val value="REDUCE_BIAS">
               </av>
               <av name=ArcInGroup descr="No Description">
                              <val value="false">
               </av>
            </oc>
            <if name=new type=if subtype=none>
               <av name=.gui.collapsed descr="No Description">
                              <val value="false">
               </av>
               <no name=new type=no subtype=none>
                  <av name=title descr="No Description">
                                    <val value="set dark exp time here">
                  </av>
                  <av name=note descr="No Description">
                                    <val value="If you changed the dark exposure time\nin the IRCAM configuration aobe remember to change it here too\n\nNB :\nThe data reduction recipe will not work unless you have a \ndark with the right exposure time\n\nUse the reset to default button to ensure the dark exposure \ntime matches that in yourIRCAM setting above,">
                  </av>
               </no>
               <ic name=new type=ic subtype=darkObs>
                  <av name=coadds descr="No Description">
                                    <val value="100">
                  </av>
                  <av name=repeatCount descr="No Description">
                                    <val value="1">
                  </av>
                  <av name=exposureTime descr="No Description">
                                    <val value="0.2">
                  </av>
               </ic>
               <no name=new type=no subtype=none>
                  <av name=title descr="No Description">
                                    <val value="how to do more than one 5-point jitters">
                  </av>
                  <av name=note descr="No Description">
                                    <val value="To repeat the quadrant jitter pattern more than once on your target , use the repeat item below to specify how many times you want to repeat it.\n\n">
                  </av>
               </no>
               <ic name=new type=ic subtype=repeat>
                  <av name=.gui.collapsed descr="No Description">
                                    <val value="false">
                  </av>
                  <no name=new type=no subtype=none>
                     <av name=title descr="No Description">
                                          <val value="offset info">
                     </av>
                     <av name=note descr="No Description">
                                          <val value="\n\nIf you want to use a different size of offset, move the highlight to the offset\nitem below and edit them.\n\nRemember that changing the number of offsets will break the reduction recipe. ">
                     </av>
                  </no>
                  <ic name=new type=ic subtype=offset>
                     <av name=Offset8 descr="No Description">
                                          <val value="-7.5">
                                          <val value="2.5">
                     </av>
                     <av name=Offset6 descr="No Description">
                                          <val value="2.5">
                                          <val value="-7.5">
                     </av>
                     <av name=Offset5 descr="No Description">
                                          <val value="2.5">
                                          <val value="-2.5">
                     </av>
                     <av name=Offset4 descr="No Description">
                                          <val value="-7.5">
                                          <val value="7.5">
                     </av>
                     <av name=Offset3 descr="No Description">
                                          <val value="-2.5">
                                          <val value="2.5">
                     </av>
                     <av name=title descr="No Description">
                                          <val value="nod_8_5as">
                     </av>
                     <av name=Offset2 descr="No Description">
                                          <val value="7.5">
                                          <val value="-7.5">
                     </av>
                     <av name=.gui.selectedOffsetPos descr="No Description">
                                          <val value="Offset0">
                     </av>
                     <av name=Offset1 descr="No Description">
                                          <val value="7.5">
                                          <val value="-2.5">
                     </av>
                     <av name=Offset0 descr="No Description">
                                          <val value="-2.5">
                                          <val value="7.5">
                     </av>
                     <av name=offsetPositions descr="No Description">
                                          <val value="Offset0">
                                          <val value="Offset1">
                                          <val value="Offset2">
                                          <val value="Offset3">
                                          <val value="Offset4">
                                          <val value="Offset5">
                                          <val value="Offset6">
                                          <val value="Offset8">
                     </av>
                     <ic name=new type=ic subtype=observe>
                        <av name=repeatCount descr="No Description">
                                                <val value="1">
                        </av>
                     </ic>
                  </ic>
               </ic>
               <no name=new type=no subtype=none>
                  <av name=title descr="No Description">
                                    <val value="last offset">
                  </av>
                  <av name=note descr="No Description">
                                    <val value="This last offset simply takes the telescope back to 00 after all the repeats of the jitter pattern are finished">
                  </av>
               </no>
               <ic name=new type=ic subtype=offset>
                  <av name=offsetPositions descr="No Description">
                                    <val value="Offset0">
                  </av>
                  <av name=Offset0 descr="No Description">
                                    <val value="0.0">
                                    <val value="0.0">
                  </av>
                  <av name=.gui.selectedOffsetPos descr="No Description">
                                    <val value="Offset0">
                  </av>
               </ic>
            </if>
         </ob>
         <ob name=new type=ob subtype=none>
            <av name=title descr="No Description">
                        <val value="JHK photometry">
            </av>
            <av name=standard descr="No Description">
                        <val value="false">
            </av>
            <av name=chainedToPrev descr="No Description">
                        <val value="false">
            </av>
            <av name=chainedToNext descr="No Description">
                        <val value="false">
            </av>
            <av name=.gui.collapsed descr="No Description">
                        <val value="true">
            </av>
            <av name=.gui.selected descr="No Description">
                        <val value="false">
            </av>
            <no name=new type=no subtype=none>
               <av name=title descr="No Description">
                              <val value="Useage">
               </av>
               <av name=note descr="No Description">
                              <val value="This sequence is an example of how to do generic n-colour photometry efficiently in a single observation.\n\nAlthough this is for JHK, you can change the filters to do any set.  ">
               </av>
            </no>
            <no name=new type=no subtype=none>
               <av name=title descr="No Description">
                              <val value="target information">
               </av>
               <av name=note descr="No Description">
                              <val value="enter your target co-ordinates into the target list below.  Note that although this is called a \"target list\" it really only allows you to specify ONE astronomical target\n\nWhen you have done so you can use the \"position editor\" to  select a guide star, to fine tune the co-ordinate based on a sky survey picture, examine offset locations on the field etc.\n\nFor a step by step guide to how to use the position editor use \"help\".">
               </av>
            </no>
            <oc name=new type=oc subtype=targetList>
               <av name=Base descr="No Description">
                              <val value="Base">
                              <val value="enter your target here">
                              <val value="">
                              <val value="">
                              <val value="FK5 (J2000)">
               </av>
               <av name=.unique descr="No Description">
                              <val value="true">
               </av>
               <av name=.gui.selectedTelescopePos descr="No Description">
                              <val value="Base">
               </av>
            </oc>
            <no name=new type=no subtype=none>
               <av name=title descr="No Description">
                              <val value="IRCAM SET UP">
               </av>
               <av name=note descr="No Description">
                              <val value="SET A DEFAULT IRCAM SETUP TO START FROM HERE">
               </av>
            </no>
            <oc name=new type=oc subtype=inst.IRCAM3>
               <av name=instPort descr="No Description">
                              <val value="West">
               </av>
               <av name=sourceMag descr="No Description">
                              <val value="11-12">
               </av>
               <av name=.unique descr="No Description">
                              <val value="true">
               </av>
               <av name=filter descr="No Description">
                              <val value="J98">
               </av>
               <av name=posAngle descr="No Description">
                              <val value="0.0">
               </av>
               <av name=coadds descr="No Description">
                              <val value="1">
               </av>
               <av name=instAper descr="No Description">
                              <val value="6.35">
                              <val value="11.77">
                              <val value="0.0">
                              <val value="1.250">
               </av>
               <av name=exposureTime descr="No Description">
                              <val value="8.0">
               </av>
               <av name=readoutArea descr="No Description">
                              <val value="256x256">
               </av>
               <av name=.gui.selected descr="No Description">
                              <val value="false">
               </av>
               <av name=acqMode descr="No Description">
                              <val value="Standard+NDSTARE">
               </av>
               <av name=.version descr="No Description">
                              <val value="1.0">
               </av>
            </oc>
            <no name=new type=no subtype=none>
               <av name=title descr="No Description">
                              <val value="DR Recipe Selection">
               </av>
               <av name=note descr="No Description">
                              <val value="The data reduction recipe specified below uses the most basic data reduction recipe for speed.  (this is a good idea if your exposure time short).\n\nThis recipe does self flat reduction for each colour.  If you do each colour more than once then the data will be co-added for each.">
               </av>
            </no>
            <oc name=new type=oc subtype=DRRecipe>
               <av name=ObjectRecipe descr="No Description">
                              <val value="JITTER5_SELF_FLAT_NCOLOUR">
               </av>
               <av name=DRRecipe descr="No Description">
                              <val value="JITTER5_SELF_FLAT">
               </av>
               <av name=DarkRecipe descr="No Description">
                              <val value="REDUCE_DARK">
               </av>
               <av name=SkyInGroup descr="No Description">
                              <val value="true">
               </av>
               <av name=.unique descr="No Description">
                              <val value="true">
               </av>
               <av name=ObjectInGroup descr="No Description">
                              <val value="true">
               </av>
               <av name=FlatRecipe descr="No Description">
                              <val value="REDUCE_FLAT">
               </av>
               <av name=DarkInGroup descr="No Description">
                              <val value="false">
               </av>
               <av name=SkyRecipe descr="No Description">
                              <val value="REDUCE_SKY">
               </av>
               <av name=ArcRecipe descr="No Description">
                              <val value="REDUCE_ARC">
               </av>
               <av name=BiasInGroup descr="No Description">
                              <val value="false">
               </av>
               <av name=title descr="No Description">
                              <val value="JITTER5_SELF_FLAT_NCOLOUR">
               </av>
               <av name=FlatInGroup descr="No Description">
                              <val value="false">
               </av>
               <av name=BiasRecipe descr="No Description">
                              <val value="REDUCE_BIAS">
               </av>
               <av name=ArcInGroup descr="No Description">
                              <val value="false">
               </av>
            </oc>
            <if name=new type=if subtype=none>
               <av name=.gui.collapsed descr="No Description">
                              <val value="false">
               </av>
               <no name=new type=no subtype=none>
                  <av name=title descr="No Description">
                                    <val value="set dark exp time here">
                  </av>
                  <av name=note descr="No Description">
                                    <val value="If you changed the dark exposure time\nin the UFTI configuration above remember to change it here too\n\nNB :\nThe data reduction recipe will not work unless you have a dark with the right exposure time\n\nThree darks are done here - one for each filter that will follow.  Set the exposure times and co-adds to match those which you will do on the source in each band.\n">
                  </av>
               </no>
               <ic name=new type=ic subtype=darkObs>
                  <av name=coadds descr="No Description">
                                    <val value="1">
                  </av>
                  <av name=repeatCount descr="No Description">
                                    <val value="1">
                  </av>
                  <av name=exposureTime descr="No Description">
                                    <val value="8.0">
                  </av>
               </ic>
               <ic name=new type=ic subtype=darkObs>
                  <av name=coadds descr="No Description">
                                    <val value="1">
                  </av>
                  <av name=repeatCount descr="No Description">
                                    <val value="1">
                  </av>
                  <av name=exposureTime descr="No Description">
                                    <val value="8.0">
                  </av>
               </ic>
               <ic name=new type=ic subtype=darkObs>
                  <av name=coadds descr="No Description">
                                    <val value="1">
                  </av>
                  <av name=repeatCount descr="No Description">
                                    <val value="1">
                  </av>
                  <av name=exposureTime descr="No Description">
                                    <val value="8.0">
                  </av>
               </ic>
               <no name=new type=no subtype=none>
                  <av name=title descr="No Description">
                                    <val value="how to do more than one 5-point jitters">
                  </av>
                  <av name=note descr="No Description">
                                    <val value="To repeat the 5 point jitter pattern more than once for each filter on your target , use the repeat item below to specify how many times you want to repeat it.\n\n">
                  </av>
               </no>
               <ic name=new type=ic subtype=repeat>
                  <av name=repeatCount descr="No Description">
                                    <val value="1">
                  </av>
                  <av name=title descr="No Description">
                                    <val value="Repeat 1X">
                  </av>
                  <av name=.gui.collapsed descr="No Description">
                                    <val value="false">
                  </av>
                  <no name=new type=no subtype=none>
                     <av name=title descr="No Description">
                                          <val value="IRCAM filter iteration">
                     </av>
                     <av name=note descr="No Description">
                                          <val value="The IRCAM iteration component below changes the filter, starting with J.  No exposure time is entered for J because it is already set in the\ngeneral IRCAM setup above.   The fitler and exposure time are then changed for H and then again for K.">
                     </av>
                  </no>
                  <ic name=new type=ic subtype=instIRCAM3>
                     <av name=iterConfigList descr="No Description">
                                          <val value="filterIter">
                                          <val value="instAperLIter">
                     </av>
                     <av name=filterIter descr="No Description">
                                          <val value="J98">
                                          <val value="H98">
                                          <val value="K98">
                     </av>
                     <av name=instAperLIter descr="No Description">
                                          <val value="1.250">
                                          <val value="1.635">
                                          <val value="2.150">
                     </av>
                     <ic name=new type=ic subtype=offset>
                        <av name=Offset4 descr="No Description">
                                                <val value="-5.0">
                                                <val value="-5.0">
                        </av>
                        <av name=.gui.selectedOffsetPos descr="No Description">
                                                <val value="Offset0">
                        </av>
                        <av name=Offset3 descr="No Description">
                                                <val value="-5.0">
                                                <val value="5.0">
                        </av>
                        <av name=Offset2 descr="No Description">
                                                <val value="5.0">
                                                <val value="-5.0">
                        </av>
                        <av name=Offset1 descr="No Description">
                                                <val value="5.0">
                                                <val value="5.0">
                        </av>
                        <av name=offsetPositions descr="No Description">
                                                <val value="Offset0">
                                                <val value="Offset1">
                                                <val value="Offset2">
                                                <val value="Offset3">
                                                <val value="Offset4">
                        </av>
                        <av name=Offset0 descr="No Description">
                                                <val value="0.0">
                                                <val value="0.0">
                        </av>
                        <ic name=new type=ic subtype=observe>
                           <av name=repeatCount descr="No Description">
                                                      <val value="1">
                           </av>
                        </ic>
                     </ic>
                  </ic>
               </ic>
               <no name=new type=no subtype=none>
                  <av name=title descr="No Description">
                                    <val value="last offset">
                  </av>
                  <av name=note descr="No Description">
                                    <val value="This last offset simply takes the telescope back to 00 after all the repeats of the jitter pattern are finished">
                  </av>
               </no>
               <ic name=new type=ic subtype=offset>
                  <av name=offsetPositions descr="No Description">
                                    <val value="Offset0">
                  </av>
                  <av name=Offset0 descr="No Description">
                                    <val value="0.0">
                                    <val value="0.0">
                  </av>
                  <av name=.gui.selectedOffsetPos descr="No Description">
                                    <val value="Offset0">
                  </av>
               </ic>
            </if>
         </ob>
      </lf>
      <lf name=new type=lf subtype=none>
         <av name=title descr="No Description">
                  <val value="UFTI+FP library">
         </av>
         <av name=.gui.collapsed descr="No Description">
                  <val value="false">
         </av>
         <ob name=new type=ob subtype=none>
            <av name=title descr="No Description">
                        <val value="online(source,sky)-offline(sky,source)">
            </av>
            <av name=standard descr="No Description">
                        <val value="false">
            </av>
            <av name=chainedToPrev descr="No Description">
                        <val value="false">
            </av>
            <av name=chainedToNext descr="No Description">
                        <val value="false">
            </av>
            <av name=.gui.collapsed descr="No Description">
                        <val value="true">
            </av>
            <no name=new type=no subtype=none>
               <av name=title descr="No Description">
                              <val value="Usage">
               </av>
               <av name=note descr="No Description">
                              <val value="This observation is designed to take FP images\nof an astronomical source, using the sequence :\n\nOn source on line\nOn sky  on line\nOn sky off line\nOn source off line\n\nThe data reduction recipe will expect the data in\nthis order and create on-line minus off-line images for both the source and the blank sky.\n\nThe offset to blank sky is set to 60,60 ">
               </av>
            </no>
            <no name=new type=no subtype=none>
               <av name=title descr="No Description">
                              <val value="target information">
               </av>
               <av name=note descr="No Description">
                              <val value="enter your target co-ordinates into the target list below.  Note that although this is called a \"target list\" it really only allows you to specify ONE astronomical target\n\nWhen you have done so you can use the \"position editor\" to  select a guide star, to fine tune the co-ordinate based on a sky survey picture, examine offset locations on the field etc.\n\nFor a step by step guide to how to use the position editor use \"help\".">
               </av>
            </no>
            <oc name=new type=oc subtype=targetList>
               <av name=Base descr="No Description">
                              <val value="Base">
                              <val value="enter your object name">
                              <val value="0:00:00">
                              <val value="0:00:00">
                              <val value="FK5 (J2000)">
               </av>
               <av name=.unique descr="No Description">
                              <val value="true">
               </av>
               <av name=.gui.selectedTelescopePos descr="No Description">
                              <val value="Base">
               </av>
            </oc>
            <no name=new type=no subtype=none>
               <av name=title descr="No Description">
                              <val value="how to set the exposure time and filter">
               </av>
               <av name=note descr="No Description">
                              <val value="The UFTI component below uses the S(1) filter.  To change it move the highlight to the \"ufti component\" below and select the filter you want to use.\n\nThe UFTI component below uses a 120 second expsoure time, which is necessary to get backgroundlimited when the FP is in the beam. To change it move the highlight to \"ufti\" below and change the exposure time.\n">
               </av>
            </no>
            <oc name=new type=oc subtype=inst.UFTI>
               <av name=instPort descr="No Description">
                              <val value="East">
               </av>
               <av name=sourceMag descr="No Description">
                              <val value="BL">
               </av>
               <av name=.unique descr="No Description">
                              <val value="true">
               </av>
               <av name=filter descr="No Description">
                              <val value="2.122">
               </av>
               <av name=posAngle descr="No Description">
                              <val value="0.0">
               </av>
               <av name=coadds descr="No Description">
                              <val value="1">
               </av>
               <av name=instAper descr="No Description">
                              <val value="0.998">
                              <val value="-0.62">
                              <val value="0.0">
                              <val value="2.122">
               </av>
               <av name=exposureTime descr="No Description">
                              <val value="120.0">
               </av>
               <av name=readoutArea descr="No Description">
                              <val value="1024x1024">
               </av>
               <av name=title descr="No Description">
                              <val value="- set configuration">
               </av>
               <av name=acqMode descr="No Description">
                              <val value="Normal+NDSTARE">
               </av>
               <av name=.version descr="No Description">
                              <val value="1.0">
               </av>
            </oc>
            <no name=new type=no subtype=none>
               <av name=title descr="No Description">
                              <val value="DR Recipe Selection">
               </av>
               <av name=note descr="No Description">
                              <val value="The data reduction recipe is matched to the observing sequence.  i.e. for online-off line reduction.\n\n">
               </av>
            </no>
            <oc name=new type=oc subtype=DRRecipe>
               <av name=ObjectRecipe descr="No Description">
                              <val value="FP">
               </av>
               <av name=DarkRecipe descr="No Description">
                              <val value="REDUCE_DARK">
               </av>
               <av name=SkyInGroup descr="No Description">
                              <val value="true">
               </av>
               <av name=.unique descr="No Description">
                              <val value="true">
               </av>
               <av name=ObjectInGroup descr="No Description">
                              <val value="true">
               </av>
               <av name=FlatRecipe descr="No Description">
                              <val value="REDUCE_FLAT">
               </av>
               <av name=DarkInGroup descr="No Description">
                              <val value="false">
               </av>
               <av name=SkyRecipe descr="No Description">
                              <val value="REDUCE_SKY">
               </av>
               <av name=ArcRecipe descr="No Description">
                              <val value="REDUCE_ARC">
               </av>
               <av name=BiasInGroup descr="No Description">
                              <val value="false">
               </av>
               <av name=title descr="No Description">
                              <val value="FP">
               </av>
               <av name=FlatInGroup descr="No Description">
                              <val value="false">
               </av>
               <av name=BiasRecipe descr="No Description">
                              <val value="REDUCE_BIAS">
               </av>
               <av name=ArcInGroup descr="No Description">
                              <val value="false">
               </av>
            </oc>
            <if name=new type=if subtype=none>
               <av name=title descr="No Description">
                              <val value="fpx, fpy set">
               </av>
               <av name=.gui.collapsed descr="No Description">
                              <val value="true">
               </av>
               <no name=new type=no subtype=none>
                  <av name=title descr="No Description">
                                    <val value="FPX and FPY settings">
                  </av>
                  <av name=note descr="No Description">
                                    <val value="After calibrating the FP in the afternoon, you should have calculated values for FPX and FPY which will set the plates parallel.\n\nEnter these new values into the component below.\n">
                  </av>
               </no>
               <ic name=new type=ic subtype=instFP>
                  <av name=title descr="No Description">
                                    <val value="FPX and FPY">
                  </av>
                  <av name=iterConfigList descr="No Description">
                                    <val value="FPX">
                                    <val value="FPY">
                                    <val value="FPZ">
                  </av>
                  <av name=FPZ descr="No Description">
                                    <val value="-630">
                  </av>
                  <av name=FPY descr="No Description">
                                    <val value="-80">
                  </av>
                  <av name=FPX descr="No Description">
                                    <val value="300">
                  </av>
               </ic>
            </if>
            <if name=new type=if subtype=none>
               <av name=title descr="No Description">
                              <val value="fpz and offsetting">
               </av>
               <av name=.gui.collapsed descr="No Description">
                              <val value="true">
               </av>
               <ic name=new type=ic subtype=repeat>
                  <av name=repeatCount descr="No Description">
                                    <val value="1">
                  </av>
                  <av name=title descr="No Description">
                                    <val value="Repeat 1X">
                  </av>
                  <av name=.gui.collapsed descr="No Description">
                                    <val value="true">
                  </av>
                  <ic name=new type=ic subtype=instFP>
                     <av name=title descr="No Description">
                                          <val value="FPZ on line">
                     </av>
                     <av name=.gui.collapsed descr="No Description">
                                          <val value="true">
                     </av>
                     <av name=iterConfigList descr="No Description">
                                          <val value="FPZ">
                     </av>
                     <av name=FPZ descr="No Description">
                                          <val value="-500">
                     </av>
                     <ic name=new type=ic subtype=observe>
                        <av name=repeatCount descr="No Description">
                                                <val value="1">
                        </av>
                     </ic>
                     <ic name=new type=ic subtype=offset>
                        <av name=offsetPositions descr="No Description">
                                                <val value="Offset1">
                        </av>
                        <av name=Offset1 descr="No Description">
                                                <val value="60.0">
                                                <val value="60.0">
                        </av>
                        <av name=.gui.selectedOffsetPos descr="No Description">
                                                <val value="Offset1">
                        </av>
                        <ic name=new type=ic subtype=sky>
                           <av name=repeatCount descr="No Description">
                                                      <val value="1">
                           </av>
                        </ic>
                     </ic>
                  </ic>
                  <ic name=new type=ic subtype=instFP>
                     <av name=title descr="No Description">
                                          <val value="FPZ offline">
                     </av>
                     <av name=.gui.collapsed descr="No Description">
                                          <val value="true">
                     </av>
                     <av name=iterConfigList descr="No Description">
                                          <val value="FPZ">
                     </av>
                     <av name=FPZ descr="No Description">
                                          <val value="-430">
                     </av>
                     <ic name=new type=ic subtype=sky>
                        <av name=repeatCount descr="No Description">
                                                <val value="1">
                        </av>
                     </ic>
                     <ic name=new type=ic subtype=offset>
                        <av name=offsetPositions descr="No Description">
                                                <val value="Offset0">
                        </av>
                        <av name=Offset0 descr="No Description">
                                                <val value="0.0">
                                                <val value="0.0">
                        </av>
                        <av name=.gui.selectedOffsetPos descr="No Description">
                                                <val value="Offset0">
                        </av>
                        <ic name=new type=ic subtype=observe>
                           <av name=repeatCount descr="No Description">
                                                      <val value="1">
                           </av>
                        </ic>
                     </ic>
                  </ic>
               </ic>
            </if>
         </ob>
      </lf>
   </lb>
</spDocument>

#!/local/perl/bin/perl

$telescope = shift or die "No argument.\n" ;
chomp( $cwd = `pwd` ) ;
chomp( $date = `date +%Y%m%d` ) ;

while( 1 )
{
	print "Install to local FTP server ?\n" ;
	$answer = <STDIN> ;
	if( $answer =~ "^y" ){ &ftp ; last ; }
	elsif( $answer =~ "^n" ){ last ; }
}

# Could do with being a lot shorter, see that repetition.
sub ftp
{
	$ftp = '/ftp/pub/jcmt/jcmtot' ;
	if( -e $ftp )
	{
		if( -e "$cwd/ot_$date.tgz" )
		{
			move( "$cwd/ot_$date.tgz" , "$ftp/ot_$date.tgz" ) 
			or die "Could not move ot_$date.tgz to $ftp/ot_$date.tgz.\n" ;
			chdir $ftp ;
	
			if( $telescope eq 'ukirt' || $telescope eq 'both' )
			{
				&relink( "$ftp/$ukirt_file.tar.gz" , "ot_$date.tgz" ) ;
			}

			if( $telescope eq 'jcmt' || $telescope eq 'both' )
			{
				&relink( "$ftp/$jcmt_file.tar.gz" , "ot_$date.tgz" ) ;
			}

			chdir $cwd ;
		}
		else
		{
			print "$cwd/ot_$date.tgz does not exist.\n" ;
		}
		
		if( -e "$cwd/ot_$date.zip" )
		{
			move( "$cwd/ot_$date.zip" , "$ftp/ot_$date.zip" )
			or die "Could not move ot_$date.zip to $ftp/ot_$date.zip.\n" ;

			chdir $ftp ;
	
			if( $telescope eq 'ukirt' || $telescope eq 'both' )
			{
				&relink( "$ftp/$ukirt_file.zip" , "ot_$date.zip" ) ;
			}

			if( $telescope eq 'jcmt' || $telescope eq 'both' )
			{
				&relink( "$ftp/$jcmt_file.zip" , "$ot_$date.zip" ) ;
			}

			chdir $cwd ;
		}
		else
		{
			print "$cwd/ot_$date.zip does not exist.\n" ;
		}

		if( -e "$cwd/JCMT-OT-$date.dmg" && ( $telescope eq 'jcmt' || $telescope eq 'both' ) )
		{
			move( "$cwd/JCMT-OT-$date.dmg" , "$ftp/JCMT-OT-$date.dmg" ) 
			or die "Could not move JCMT-OT-$date.dmg to $ftp/JCMT-OT-$date.dmg.\n" ;
			chdir $ftp ;
			&relink( "$ftp/JCMT-OT-$date.dmg" , "JCMT-OT.dmg" ) ;
			chdir $cwd ;
		}
		else
		{
			unless( -e "$cwd/JCMT-OT-$date.dmg" )
			{
				print "$cwd/JCMT-OT-$date.dmg does not exist\n" ;
			}
		}

		if( -e "$cwd/UKIRT-OT-$date.dmg" && ( $telescope eq 'ukirt' || $telescope eq 'both' ) )
		{
			move( "$cwd/UKIRT-OT-$date.dmg" , "$ftp/UKIRT-OT-$date.dmg" ) 
			or die "Could not move UKIRT-OT-$date.dmg to $ftp/UKIRT-OT-$date.dmg.\n" ;
			chdir $ftp ;
			&relink( "$ftp/UKIRT-OT-$date.dmg" , "UKIRT-OT.dmg" ) ;
			chdir $cwd ;
		}
		else
		{
			unless( -e "$cwd/UKIRT-OT-$date.dmg" )
			{
				print "$cwd/UKIRT-OT-$date.dmg does not exist\n" ;
			}
		}
	}
	else
	{
		print "FTP not available.\n" 
	}
}

sub relink
{
	$target = shift or die "No target defined for relinking.\n" ;
	$source = shift or die "No source defined for relinking.\n" ;

	unlink $target or die "Could not unlink $target.\n" ;
	symlink $source , $target or die "Error relinking $target.\n" ;
}
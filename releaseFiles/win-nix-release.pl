#!/usr/bin/perl

use File::Copy ;

$telescope = shift or die "No argument.\n" ;

chomp( $cwd = `pwd` ) ;

$ukirt_file = 'ot_latest' ;
$jcmt_file = "jcmt$ukirt_file" ;

if( -e 'ompot' )
{ 
	die "Couldn't remove ompot.\n" 
	if system( 'rm' , ( '-rf' , "$cwd/ompot" ) ) ; 
}

chdir( '..' ) or die "Could not cd .. \n" ;

@make_args = ( 'make' , 'clean' ) ;
if( system @make_args ){ die "Could not clean.\n" ; }
@make_args = ( 'make' , 'install' ) ;
if( system @make_args ){ die "Could not build.\n" ; }

rename 'install' , "$cwd/ompot" or die "Could not rename install.\n" ;;
chdir( $cwd ) or die "Could not cd $cwd \n" ;

if( $telescope eq 'ukirt' || $telescope eq 'both' )
{
	my @cp_args = ( "$cwd/ukirtot" , "$cwd/ompot/bin/ukirtot" ) ;
	die "Could not rename ukirtot.\n"
	if system( 'cp' , @cp_args ) ;
}
if( $telescope eq 'jcmt' || $telescope eq 'both' )
{
	my @cp_args = ( "$cwd/jcmtot" , "$cwd/ompot/bin/jcmtot" ) ;
	die "Could not rename jcmtot.\n"
	if system( 'cp' , @cp_args ) ;
}

chomp( my $date = `date +%Y%m%d` ) ;

@tar_args = ( 'tar' , 'czvf' , "ot_$date.tgz" , 'ompot' ) ;
if( system @tar_args ){ die "Could not tar for some reason.\n" ; }

@zip_args = ( 'zip' , '-r' , "ot_$date.zip" , 'ompot' ) ;
if( system @zip_args ){ die "Could not zip for some reason.\n" ; }

print "Done.\n" ;

sub relink
{
	$target = shift or die "No target defined for relinking.\n" ;
	$source = shift or die "No source defined for relinking.\n" ;

	unlink $target or die "Could not unlink $target.\n" ;
	symlink $source , $target or die "Error relinking $target.\n" ;
}

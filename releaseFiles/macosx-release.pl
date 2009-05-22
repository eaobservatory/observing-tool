#!/usr/bin/perl

chomp( $os = `uname -s` ) ;
unless( $os eq 'Darwin' ){ die "Mac OS X required.\n" }

$uc_telescope = shift ;
$uc_telescope = uc( $uc_telescope ) ;
$lc_telescope = $uc_telescope ;
$lc_telescope = lc( $lc_telescope ) ;
@telescopes = ( 'UKIRT' , 'JCMT' ) ;
$match = 0 ;
foreach $scope ( @telescopes ){ if( $uc_telescope eq $scope ){ $match = 1 ; } }
if( $match == 0 ){ die "\'$uc_telescope\' not in @telescopes.\n" ; }

chomp( $cwd = `pwd` ) ;

$release = $cwd . '/' . 'ompot' ;
unless( -e $release ){ die "Cannot find release : $release \n" ; }

chomp( $date = `date +%Y%m%d` ) ;

$folder = $cwd . '/' . $uc_telescope . '-' . 'OT' . '-' . $date  ;
if( -e $folder ){ "Could not rm $folder.\n" if system( 'rm' , ( '-rf' , $folder ) ) ; }

$skeletons = "$cwd/skeletons" ;

@cp_args = ( '-r' , "$skeletons/$uc_telescope/" , $folder ) ;
die "Cannot cp @cp_args\n" if system( 'cp' , @cp_args ) ;

$app = "$folder/$uc_telescope-OT.app" ;

@cp_args = ( "$skeletons/$lc_telescope\_icon.icns" , "$app/Contents/Resources" ) ;
die "Cannot cp @cp_args\n" if system( 'cp' , @cp_args ) ;

@classpath = () ;
@jars = `find $release -name "*.jar"` ;
@schema = `find $release -name schema` ;
@cfg = `find $release -name $lc_telescope` ;
push( @jars , @schema ) ;
push( @jars , @cfg ) ;
foreach $jar ( @jars ){ &cp_push( $jar ) ; }

sub cp_push
{
	my $file = shift ;
	chomp( $file ) ;
	my $dest = "$app/Contents/Resources/Java/" ;
	system( 'cp' , ( '-r' , $file , $dest ) ) ;
	my @path = split( '/' , $file ) ;
	$file = pop( @path ) ;
	my $line = "<string>\$JAVAROOT/$file</string>" ;
	push( @classpath , $line ) ;
}

open( INFO , '<' , "$skeletons/Info.plist" ) ;
@contents = <INFO> ;
close INFO ;

&write_file( "$app/Contents/Info.plist" ) ;

open( INFO , '<' , "$skeletons/README.rtf" ) ;
@contents = <INFO> ;
close INFO ;

&write_file( "$folder/README.rtf" ) ;

@hdiutil_args = ( 'hdiutil' , 'create' , '-srcfolder' , $folder , "$folder.dmg" ) ;
die "Cannot @hdiutil_args \n" if system( @hdiutil_args ) ;
die "Cannot rm $folder \n" if system( 'rm' , ( '-rf' , $folder ) ) ;

sub write_file
{
	my $file = shift ;
	open( INFO , '>' , $file ) ;
	foreach $line ( @contents )
	{
		if( $line =~ '<!-- telescope_long -->' )
		{
			$telescope_long = '' ;
			if( $uc_telescope eq 'UKIRT' ){ $telescope_long = 'United Kingdom Infra Red' ; }
			elsif( $uc_telescope eq 'JCMT' ){ $telescope_long = 'James Clerk Maxwell' ; }
			$line =~ s/\<!-- telescope_long -->/$telescope_long/g ;
		}
		if( $line =~ '<!-- uc_telescope -->' )
		{
			$line =~ s/\<!-- uc_telescope -->/$uc_telescope/g ;
		}
		if( $line =~ '<!-- lc_telescope -->' )
		{
			$line =~ s/\<!-- lc_telescope -->/$lc_telescope/g ;
		}
		if( $line =~ '<!-- classpath -->' )
		{
			$line =~ s/\<!-- classpath -->//g ;
			my $length = length( $line ) ;
			my $tmp = '' ;
			foreach $entry ( @classpath )
			{
				$tmp .= "\t" x $length . $entry . "\n" ;
			}
			$line = $tmp ;
		}
		if( $line =~ '<!-- date -->' )
		{
			$line =~ s/\<!-- date -->/$date/g ;
		}
		if( $line =~ '<!-- build_date -->' )
		{
			open( VERSION , '<' , "$release/cfg/ot/$lc_telescope/versionFile" ) ;
			@version_lines = <VERSION> ;
			close VERSION ;
			$build_date = join( '' , @version_lines ) ;
			chomp( $build_date ) ;
			$line =~ s/\<!-- build_date -->/$build_date/g ;
		}
		if( $line =~ '<!-- macosx_version -->' )
		{
			chomp( $macosx_version = `sw_vers -productVersion` ) ;
			$line =~ s/\<!-- macosx_version -->/$macosx_version/g ;
		}
		if( $line =~ '<!-- java_runtime -->' )
		{
			$line =~ s/\<!-- java_runtime -->/1.5/g ;
		}

		print INFO $line ;
	}
	close INFO ;
}

